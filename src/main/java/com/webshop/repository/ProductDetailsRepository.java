package com.webshop.repository;

import com.webshop.entity.ProductDetails;
import org.springframework.data.repository.CrudRepository;

public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {

    ProductDetails findByCode(String code);
}
