package com.webshop.kung.service;

import com.webshop.kung.entity.Order;

import java.util.List;

public interface OrderService {
    public Order addOrder(Order order);
    public Order findById(Long id);
    public List<Order> getAllOrders();
    public List<Order> findOrdersByUsername(String username);
}
