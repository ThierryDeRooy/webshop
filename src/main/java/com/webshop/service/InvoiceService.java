package com.webshop.service;

import com.webshop.entity.Order;
import com.webshop.entity.OrderCost;
import com.webshop.entity.OrderDetails;
import com.webshop.model.invoice.Orderline;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MessageSource messageSource;
    Logger log = LogManager.getLogger(InvoiceService.class);

    // Path to the jrxml template
    private final String invoice_template_path = "/jasper/invoice_template.jrxml";
    private static final String logo_path = "/jasper/images/Kung-Thaise-Groente-rechthoek-Logo-250px.png";

    public File generateInvoiceFor(Order order, Locale locale) throws IOException {

        if (order.getReceiverBtw()==null)
            order.setReceiverBtw("");

        // Create a temporary PDF file
        File pdfFile = File.createTempFile("my-invoice", ".pdf");

        // Initiate a FileOutputStream
        try(FileOutputStream pos = new FileOutputStream(pdfFile))
        {
            // Load the invoice jrxml template.
            final JasperReport report = loadTemplate();

            // Create parameters map.
            final Map<String, Object> parameters = parameters(order, locale);

            // Create an empty datasource.
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));

            // Render the PDF file
            JasperReportsUtils.renderAsPdf(report, parameters, dataSource, pos);

            log.info("PDF file generated: " + pdfFile.getAbsolutePath());
            return pdfFile;
        }
        catch (final Exception e)
        {
            log.error(String.format("An error occured during PDF creation: %s", e));
        }
        return null;
    }

    // Load invoice jrxml template
    private JasperReport loadTemplate() throws JRException {

        log.info(String.format("Invoice template path : %s", invoice_template_path));

        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);

        return JasperCompileManager.compileReport(jasperDesign);
    }

    // Fill template order parametres
    private Map<String, Object> parameters(Order order, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("logo", getClass().getResourceAsStream(logo_path));
        parameters.put("order", order);

        JRBeanCollectionDataSource orderlines = new JRBeanCollectionDataSource(getOrderlines(order));
        parameters.put("orderlines", orderlines);
        JRBeanCollectionDataSource summaryList = new JRBeanCollectionDataSource(generateSummary(order, locale));
        parameters.put("summaries", summaryList);
//        parameters.put("summaries", generateSummary(order, locale));
        parameters.put("REPORT_LOCALE", locale);

        return parameters;
    }

    private List<Orderline> getOrderlines(Order order) {
        List<Orderline> orderlines = new ArrayList<>();
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            orderlines.add(new Orderline(orderDetails, messageSource));
        }
        for (OrderCost orderCost : order.getOrderCosts()) {
            orderlines.add(new Orderline(orderCost, messageSource));
        }
        return orderlines;
    }


    private List<Summary> generateSummary(Order order, Locale locale) {
        Map<BigDecimal,BigDecimal[]> summaryLijst = new TreeMap<BigDecimal, BigDecimal[]>();
        List<Summary> summaries = new ArrayList<>();
        summaries.add(new Summary(messageSource.getMessage("total.exclBTW", null, locale), order.getTotalPriceInclTransport()));
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            if (orderDetails.getBtw().doubleValue()>0) {
                BigDecimal[] values = new BigDecimal[2];
                if (summaryLijst.containsKey(orderDetails.getBtw())) {
                    values[0] = summaryLijst.get(orderDetails.getBtw())[0].add(orderDetails.getTotalPrice());
                    values[1] = summaryLijst.get(orderDetails.getBtw())[1].add(orderDetails.getTotalPriceInclBtw()).subtract(orderDetails.getTotalPrice());
                    summaryLijst.replace(orderDetails.getBtw(), values);
                } else {
                    values[0] = orderDetails.getTotalPrice();
                    values[1] = orderDetails.getTotalPriceInclBtw().subtract(orderDetails.getTotalPrice());
                    summaryLijst.put(orderDetails.getBtw(), values);
                }
            }
        }
        for (Map.Entry<BigDecimal, BigDecimal[]> entry : summaryLijst.entrySet()) {
            summaries.add(new Summary( String.format(messageSource.getMessage("invoice.vat", null, locale), entry.getKey().toString(), entry.getValue()[0].toString()),
                    entry.getValue()[1]));
        }
        summaries.add(new Summary(messageSource.getMessage("total.amount", null, locale), order.getTotalPriceInclBtwTransport()));
        return summaries;

    }


    public class Summary{
        private String description;
        private String bedrag;

        public Summary(String description, BigDecimal bedrag) {
            setDescription(description);
            setBedrag("€ " + bedrag.toString());
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBedrag() {
            return bedrag;
        }

        public void setBedrag(String bedrag) {
            this.bedrag = bedrag;
        }
    }
}
