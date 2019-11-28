package com.webshop.kung.repository;

import com.webshop.kung.entity.ProductDetails;
import org.springframework.data.repository.CrudRepository;

public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {

    ProductDetails findByCode(String code);
}
