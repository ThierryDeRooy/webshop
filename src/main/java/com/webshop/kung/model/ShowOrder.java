package com.webshop.kung.model;

import com.webshop.kung.entity.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShowOrder {
    private Order order;
    private List<ShowOrderLine> orderLines = new ArrayList<>();

    public ShowOrder(Order order) {
        setOrder(order);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ShowOrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<ShowOrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void addOrderLine(ShowOrderLine showOrderLine) {
        orderLines.add(showOrderLine);
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (ShowOrderLine showOrderLine : orderLines) {
            totalPrice = totalPrice.add(showOrderLine.getTotalPrice());
        }
        return totalPrice;
    }

}
