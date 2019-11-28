package com.webshop.kung.service;

import com.webshop.kung.entity.OrderDetails;
import com.webshop.kung.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public OrderDetails addOrderDetails(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }
}
