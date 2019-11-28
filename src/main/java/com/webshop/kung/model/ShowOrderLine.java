package com.webshop.kung.model;

import com.webshop.kung.entity.OrderDetails;

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

        return orderDetails.getPrice().multiply(new BigDecimal(orderDetails.getQuantity()));
    }

}
