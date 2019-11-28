package com.webshop.kung.service;

import com.webshop.kung.entity.Order;
import com.webshop.kung.entity.User;
import com.webshop.kung.repository.OrderRepository;
import com.webshop.kung.repository.UserRepository;
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
    private UserRepository userRepository;

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
    public List<Order> getAllOrders() {
        Iterable<Order> orderIterable = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        orderIterable.forEach(orders::add);
        return orders;
    }

    @Override
    public List<Order> findOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return new ArrayList<>();
        return orderRepository.findByCustomer(user);
    }
}
