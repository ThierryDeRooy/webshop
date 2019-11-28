package com.webshop.kung.repository;

import com.webshop.kung.entity.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
