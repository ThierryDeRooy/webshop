package com.webshop.kung.repository;

import com.webshop.kung.entity.Order;
import com.webshop.kung.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
}
