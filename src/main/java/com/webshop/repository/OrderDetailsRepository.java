package com.webshop.repository;

import com.webshop.entity.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
