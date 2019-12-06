package com.webshop.kung.repository;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(Category category);
//    Product findByCode(String code);
    List<Product> findByLangAndName(Locale lang, String name);
    List<Product> findByLang(Locale lang);
    Optional<Product> findById(Long id);

    @Query("SELECT P FROM Product P WHERE P.productDetails.status=:status AND P.lang=:locale")
    List<Product> getByLangAndStatus(Locale locale, Integer status);

    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale")
    Product getByCode(Locale locale, String code);
    
    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale")
    Product findByCode(Locale locale, String code);

    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale AND P.productDetails.status=:status")
    Product getByCodeAndStatus(Locale locale, String code, Integer status);

//    @Query("SELECT P.* FROM product P, LOCALISED_STRINGS L WHERE P.NAME_ID=L.LOCALISED_ID AND L.STRINGS_KEY=:locale AND L.STRINGS LIKE %:name%")
//    List<Product> getByName(String locale, String name);

    @Query("SELECT prod FROM Product prod where prod.lang<>:locale AND prod.productDetails.code NOT in (SELECT prod1.productDetails.code FROM Product prod1 where prod1.lang=:locale)")
    List<Product> getMissingProducts(Locale locale);
}
