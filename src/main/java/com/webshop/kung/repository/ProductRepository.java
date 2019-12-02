package com.webshop.kung.repository;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Locale;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(Category category);
//    Product findByCode(String code);
    List<Product> findByLangAndName(Locale lang, String name);
    List<Product> findByLang(Locale lang);

    @Query("SELECT P.* FROM product P, product_details PD WHERE PD.status=:status AND P.lang=:locale AND PD.code=P.code")
    List<Product> getByLangAndStatus(String locale, Integer status);

    @Query("SELECT P.* FROM product P, product_details PD WHERE PD.code=:code AND P.lang=:locale AND PD.code=P.code")
    Product getByCode(String locale, String code);

    @Query("SELECT P.* FROM product P, product_details PD WHERE PD.code=:code AND P.lang=:locale AND PD.code=P.code AND PD.status=:status")
    Product getByCodeAndStatus(String locale, String code, Integer status);

    @Query("SELECT P.* FROM product P, LOCALISED_STRINGS L WHERE P.NAME_ID=L.LOCALISED_ID AND L.STRINGS_KEY=:locale AND L.STRINGS LIKE %:name%")
    List<Product> getByName(String locale, String name);

    @Query("SELECT prod.* FROM product prod where prod.lang<>:locale AND prod.code NOT in (SELECT prod1.code FROM product prod1 where prod1.lang=:locale)")
    List<Product> getMissingProducts(String locale);
}
