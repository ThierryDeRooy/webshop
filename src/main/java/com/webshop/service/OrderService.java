package com.webshop.service;

import com.webshop.entity.Order;

import java.util.List;

public interface OrderService {
    public Order addOrder(Order order);
    public Order findById(Long id);
    public Order findByIdAndUsernameAndStatusNot(Long id, String username, int status);
    public List<Order> getAllOrders();
    public List<Order> findOrdersByUsername(String username);
    public List<Order> findOrdersByUsernameAndStatus(String username, int status);
    public List<Order> findOrdersByUsernameAndStatusNot(String username, int status);
    public List<Order> findOrdersByStatus(int status);
    public List<Order> findOrdersByStatusAndCustomerData(int status, String search);
    public List<Order> findOrdersByCustomerData(String search);
    public void deleteOrder(long id);
}
