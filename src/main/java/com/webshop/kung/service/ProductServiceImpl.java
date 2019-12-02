package com.webshop.kung.service;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;
import com.webshop.kung.repository.ProductDetailsRepository;
import com.webshop.kung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Override
    public List<Product> findAllProducts() {
        return (List<Product>)productRepository.findAll();
    }

    @Override
    @Cacheable("Product")
    public List<Product> findAllProducts(Locale locale) {
        return (List<Product>)productRepository.findByLang(locale);
    }

    @Override
    public List<Product> findMissingProducts(Locale locale) {
        return productRepository.getMissingProducts(locale);
    }

    @Override
    public List<Product> findAllProductsByCategory(Category category) {
        List<Product> productList = productRepository.findByCategory(category);
        return productList;
    }

    @Override
    public Product addProduct(Product product) {
        productDetailsRepository.save(product.getProductDetails());
        return productRepository.save(product);
    }

    @Override
    public ProductDetails saveDetails(ProductDetails productDetails) {
        return productDetailsRepository.save(productDetails);
    }

    @Override
    public ProductDetails findByCode(String code) {
        return productDetailsRepository.findByCode(code);
    }

    @Override
    @Cacheable("Product")
    public Product findByCode(Locale lang, String code) {
        Long prodId = productRepository.findByCode(lang, code);
        Optional<Product> optProd = productRepository.findById(prodId);
        if (optProd == null) 
            return null;
        return optProd.get();
    }

    @Override
    public Product findByCodeAndStatus(Locale lang, String code, Integer status) {
        return productRepository.getByCodeAndStatus(lang, code, status);
    }

    @Override
    public List<Product> findByLocaleName(Locale locale, String name) {
        return productRepository.findByLangAndName(locale, name);
    }

    @Override
    public List<Product> findAllProductsByStatus(Locale locale, Integer status) {
        return productRepository.getByLangAndStatus(locale, status);
    }
}
