package com.webshop.service;

import com.webshop.entity.Order;
import com.webshop.entity.OrderCost;
import com.webshop.entity.OrderDetails;
import com.webshop.entity.WebUser;
import com.webshop.model.invoice.Orderline;
import com.webshop.utils.VelocityEngineUtils;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import java.io.*;
import java.util.*;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${mail.from.name}")
    private String mailFromName;
    @Value("${mail.from.email}")
    private String mailFromEmail;


    @Override
    public void sendOrderMail(Order order) throws MessagingException {
        sendOrderMail(order, null);
    }


//    public void sendOrderMail4(Order order, File fileAttach) throws MessagingException {
//        Locale loc = LocaleContextHolder.getLocale();
//        MimeMessage message = mailSender.createMimeMessage();
//        message.setSubject(messageSource.getMessage("order.title",null, loc) + " " + order.getId());
//
//        MimeMessageHelper helper;
//        helper = new MimeMessageHelper(message, true);
//        helper.setTo(order.getReceiverEmail());
//        try {
//            helper.setFrom(new InternetAddress(mailFromEmail, mailFromName));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("title", messageSource.getMessage("order.title",null, loc) + " " + order.getId());
//        model.put("status", messageSource.getMessage("lang.state",null, loc) + " " + Constants.ORDER_STATES.get(order.getStatus()));
//        model.put("smallContent", messageSource.getMessage("lang.total.quantity",null, loc) + ": " + order.getQuantity() + ", " +
//                messageSource.getMessage("lang.totalPrice",null, loc) + ": " + order.getTotalPrice() + " EUR");
//        model.put("code", messageSource.getMessage("lang.code",null, loc));
//        model.put("productName",  messageSource.getMessage("lang.productName",null, loc));
//        model.put("price", messageSource.getMessage("lang.price",null, loc) + "(EUR)");
//        model.put("vat", messageSource.getMessage("lang.BTW",null, loc) + "(EUR)");
//        model.put("quantity", messageSource.getMessage("lang.quantity",null, loc));
//        model.put("totalPrice", messageSource.getMessage("lang.totalPrice",null, loc) + "(EUR)");
//        model.put("dearCustomer", messageSource.getMessage("mail.dear.customer",null, loc));
//        model.put("content", messageSource.getMessage("mail.content",null, loc));
//        model.put("thanks", messageSource.getMessage("mail.thanks",null, loc));
//        model.put("order", order);
////        model.put("body",text);
//
//        VelocityEngine velocityEngine = new VelocityEngine();
//        velocityEngine.init();
//        String textMsg = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "src/main/webapp/WEB-INF/view/templates/email/orderConfirm.vm", "UTF-8", model);
//
//        helper.setText(textMsg, true);
////        File fileLogo = new File("src/main/resources/static/img/Kung-Thaise-Groente-rechthoek-Logo-250px.png");
////        helper.addAttachment("Logo.png", fileLogo);
//        if (fileAttach != null) {
//            helper.addAttachment(fileAttach.getName(), fileAttach);
//        }
//        mailSender.send(message);
//    }
    @Override
    public void sendOrderMail(Order order, File fileAttach) throws MessagingException {
        Map<String, String> params = new TreeMap<>();
        params.put("title" , "order.title");
        params.put("content" , "mail.content");
        sendOrderMail(order, fileAttach, params);
    }

    @Override
    public void resendOrderMail(Order order) throws MessagingException {
        sendOrderMail(order, null);
    }

    @Override
    public void resendOrderMail(Order order, File fileAttach) throws MessagingException {
        Map<String, String> params = new TreeMap<>();
        params.put("title" , "order.reminder.title");
        params.put("content" , "mail.Reminder.content");
        sendOrderMail(order, fileAttach, params);
    }

    @Override
    public void sendUsername(WebUser webUser) throws MessagingException {
        Locale loc = LocaleContextHolder.getLocale();
        MimeMessage message = mailSender.createMimeMessage();
        message.setSubject("WEBSHOP - " + messageSource.getMessage("lang.username",null, loc));
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(webUser.getEmail());
        try {
            helper.setFrom(new InternetAddress(mailFromEmail, mailFromName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String textMsg = messageSource.getMessage("login.username.use",null, loc) + " " + webUser.getUsername();
        helper.setText(textMsg, false);
        mailSender.send(message);
    }

    private void sendOrderMail(Order order, File fileAttach, Map<String, String> params) throws MessagingException {
        Locale loc = LocaleContextHolder.getLocale();
        MimeMessage message = mailSender.createMimeMessage();
        message.setSubject(messageSource.getMessage("order.title",null, loc) + " " + order.getId());
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
        helper.setTo(order.getReceiverEmail());
        try {
            helper.setFrom(new InternetAddress(mailFromEmail, mailFromName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", messageSource.getMessage(params.get("title"),null, loc) + " " + order.getId());
//        model.put("status", messageSource.getMessage("lang.state",null, loc) + " " + Constants.ORDER_STATES.get(order.getStatus()));
        model.put("smallContent", messageSource.getMessage("lang.total.quantity",null, loc) + ": " + order.getQuantity() + ", " +
                messageSource.getMessage("lang.totalPrice",null, loc) + ": " + order.getTotalPriceInclBtwTransport() + " EUR");
        model.put("code", messageSource.getMessage("lang.code",null, loc));
        model.put("productName",  messageSource.getMessage("lang.description",null, loc));
        model.put("price", messageSource.getMessage("lang.price",null, loc) + "(EUR)");
        model.put("quantity", messageSource.getMessage("lang.quantity",null, loc));
        model.put("vat", messageSource.getMessage("lang.BTW",null, loc) + "(EUR)");
        model.put("totalPrice", messageSource.getMessage("lang.totalPrice",null, loc) + "(EUR)");
        model.put("dearCustomer", messageSource.getMessage("mail.dear.customer",null, loc));
        model.put("content", messageSource.getMessage(params.get("content"),null, loc));
        model.put("thanks", messageSource.getMessage("mail.thanks",null, loc));
        List<Orderline> orderlines = new ArrayList<>();
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            orderlines.add(new Orderline(orderDetails, messageSource));
        }
        for (OrderCost orderCost : order.getOrderCosts()) {
            orderlines.add(new Orderline(orderCost, messageSource));
        }
        model.put("orderlines", orderlines);
//        model.put("body",text);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }


        // --Create the HTML body part of the message
        MimeBodyPart mimeBody = new MimeBodyPart();
        String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "src/main/webapp/WEB-INF/view/templates/email/orderConfirm.vm", "UTF-8", model);
        mimeBody.setContent(body, "text/html");


        // --Create the image part of the message
        MimeBodyPart mimeImage = new MimeBodyPart();
        DataSource ds = new URLDataSource(classLoader.getResource("static/img/Kung-Thaise-Groente-rechthoek-Logo-250px.png"));
        mimeImage.setDataHandler(new DataHandler(ds));
        mimeImage.setHeader("Content-ID", "Logo");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBody);
        multipart.addBodyPart(mimeImage);

        // --Create the pdf attachment
        FileInputStream fis = null;
        MimeBodyPart attachment= new MimeBodyPart();
        try {
            fis = new FileInputStream(fileAttach);
            ByteArrayDataSource bds = new ByteArrayDataSource(fis, "application/pdf");
            attachment.setDataHandler(new DataHandler(bds));
            attachment.setFileName(fileAttach.getName());
            multipart.addBodyPart(attachment);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }


        message.setContent(multipart);

        mailSender.send(message);
    }


//    private String createTable(List<OrderDetails> orderItems, Locale loc) {
////        String result = "<table style='border: 1px solid black;border-spacing:10px 0;'><tr>";
//        String result = "<table class='productTable'><tr>";
//        result = result + "<th>" + messageSource.getMessage("lang.code",null, loc) + "</th>";
//        result = result + "<th>" + messageSource.getMessage("lang.productName",null, loc) + "</th>";
//        result = result + "<th>" + messageSource.getMessage("lang.price",null, loc) + "(EUR)</th>";
////        result = result + "<th>" + messageSource.getMessage("lang.unit",null, loc) + "</th>";
//        result = result + "<th>" + messageSource.getMessage("lang.quantity",null, loc) + "</th>";
//        result = result + "<th>" + messageSource.getMessage("lang.totalPrice",null, loc) + "(EUR)</th>";
//        result = result + "</tr>";
//
//        for (OrderDetails item : orderItems) {
//            Product prod = item.getProduct();
//            result = result + "<tr>";
//            result = result + "<td>" + prod.getProductDetails().getCode() + "</td>";
//            result = result + "<td>" + item.getProductName() + "</td>";
//            result = result + "<td>" + item.getPrice() + " " + messageSource.getMessage("lang.per"+Constants.PRODUCT_UNITS.get(item.getUnit()),null, loc) + "</td>";
////            result = result + "<td>" + Constants.PRODUCT_UNITS.get(item.getUnit()) + "</td>";
//            result = result + "<td>" + item.getQuantity() + "</td>";
//            result = result + "<td>" + item.getTotalPrice() + "</td>";
//            result = result + "</tr>";
//
//        }
//
//        result = result + "</table>";
//        return result;
//    }

//    public String process(String template, Map<String,String> params) {
//
//    }
}
