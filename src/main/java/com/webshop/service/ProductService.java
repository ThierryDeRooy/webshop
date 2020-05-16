package com.webshop.service;

import com.webshop.entity.Category;
import com.webshop.entity.ProductDetails;
import com.webshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public interface ProductService {
//    List<Product> findAllProducts();
    List<Product> findAllProducts(Locale locale);
    List<Product> findMissingProducts(Locale locale);
//    List<Product> findAllProductsByCategory(Category category);
    Product addProduct(Product product);
//    Product addProductOnly(Product product);
    void updateProduct(Locale locale, Product product, MultipartFile file, int addToStock) throws IOException;
    void updateStock(ProductDetails productDetails, int stock);
//    void saveDetails(ProductDetails productDetails, int stock);
//    ProductDetails findByCode(String code);
    ProductDetails findById(Long id);
    Product findByCode(Locale loc, String code);
//    Product findByLangAndCode(Locale loc, String code);
//    List<Product> findByLocaleName(Locale locale, String name);
//    List<Product> findAllProductsByStock(Locale locale, int stock);
    Page<Product> getAllProductsByStock(Locale locale, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction, String search, Category category);;
}
