package com.webshop.model.invoice;

import com.webshop.constants.Constants;
import com.webshop.entity.OrderCost;
import com.webshop.entity.OrderDetails;
import lombok.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.Locale;

@Data
public class Orderline {

    private String description;
    private String code;
    private int quantity;
    private String unit;
    private BigDecimal price;
    private BigDecimal vat;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceInclVat;
    
    public Orderline(OrderDetails orderDetails, MessageSource messageSource) {
        setCode(orderDetails.getProduct().getProductDetails().getCode());
        setDescription(orderDetails.getProductName());
        setQuantity(orderDetails.getQuantity());
        String unit = Constants.PRODUCT_UNITS.get(orderDetails.getUnit());
        Locale loc = LocaleContextHolder.getLocale();
        setUnit(messageSource.getMessage("lang." + unit,null, loc));
        setPrice(orderDetails.getPrice());
        setVat(orderDetails.getBtw());
        setTotalPrice(orderDetails.getTotalPrice());
        setTotalPriceInclVat(orderDetails.getTotalPriceInclBtw());
    }

    public Orderline(OrderCost orderCost, MessageSource messageSource) {
        setCode("");
        setDescription(orderCost.getDescription());
        setQuantity(orderCost.getQuantity());
        String unit = Constants.PRODUCT_UNITS.get(0);
        Locale loc = LocaleContextHolder.getLocale();
        setUnit(messageSource.getMessage("lang." + unit,null, loc));
        setPrice(orderCost.getPrice());
        setVat(orderCost.getTotalVat());
        setTotalPrice(orderCost.getTotalPrice());
        setTotalPriceInclVat(orderCost.getTotalPriceInclVat());
    }

    public BigDecimal getTotalVat(){
        return getTotalPriceInclVat().subtract(getTotalPrice());
    }

}
