package com.webshop.kung.service;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;

import java.util.List;
import java.util.Locale;

public interface ProductService {
    List<Product> findAllProducts();
    List<Product> findAllProducts(Locale locale);
    List<Product> findMissingProducts(Locale locale);
    List<Product> findAllProductsByCategory(Category category);
    Product addProduct(Product product);
    ProductDetails saveDetails(ProductDetails productDetails);
    ProductDetails findByCode(String code);
    Product findByCode(String lang, String code);
    Product findByCodeAndStatus(String lang, String code, Integer status);
    List<Product> findByLocaleName(Locale locale, String name);
    List<Product> findAllProductsByStatus(Locale locale, Integer status);
}
