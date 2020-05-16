package com.webshop.model;

import com.webshop.entity.OrderDetails;

import java.math.BigDecimal;

public class ShowOrderLine {

    private OrderDetails orderDetails;

    public ShowOrderLine(OrderDetails orderDetails) {
        setOrderDetails(orderDetails);
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public BigDecimal getTotalPrice() {

        return orderDetails.getTotalPrice();
    }
    public BigDecimal getTotalPriceInclBtw() {

        return orderDetails.getTotalPriceInclBtw();
    }

}
