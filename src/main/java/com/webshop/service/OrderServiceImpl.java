package com.webshop.service;

import com.webshop.entity.Customer;
import com.webshop.entity.Order;
import com.webshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        Order order = orderOpt.get();
        return order;
    }

    @Override
    public Order findByIdAndUsernameAndStatusNot(Long id, String username, int status) {
        Customer customer = customerService.findByUsername(username);
        if (customer == null)
            return null;
        return orderRepository.findByIdAndCustomerAndStatusNot(id, customer, status);
    }

    @Override
    public List<Order> getAllOrders() {
        Iterable<Order> orderIterable = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        orderIterable.forEach(orders::add);
        return orders;
    }

    @Override
    public List<Order> findOrdersByUsername(String username) {
        Customer customer = customerService.findByUsername(username);
        if (customer == null)
            return new ArrayList<>();
        return orderRepository.findByCustomer(customer);
    }

    @Override
    public List<Order> findOrdersByUsernameAndStatus(String username, int status) {
        Customer customer = customerService.findByUsername(username);
        if (customer == null)
            return new ArrayList<>();
        return orderRepository.findByCustomerAndStatus(customer, status);
    }

    @Override
    public List<Order> findOrdersByUsernameAndStatusNot(String username, int status) {
        Customer customer = customerService.findByUsername(username);
        if (customer == null)
            return new ArrayList<>();
        return orderRepository.findByCustomerAndStatusNot(customer, status);
    }

    @Override
    public List<Order> findOrdersByStatus(int status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findOrdersByStatusAndCustomerData(int status, String search) {
        return orderRepository.searchByStatusAndCustomerData(status, search.toLowerCase());
    }

    @Override
    public List<Order> findOrdersByCustomerData(String search) {
        return orderRepository.searchByCustomerData(search.toLowerCase());
    }

    @Override
    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }
}
