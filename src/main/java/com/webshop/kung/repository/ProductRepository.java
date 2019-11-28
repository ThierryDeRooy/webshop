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


    @Query(value = "SELECT P.* FROM PRODUCT P JOIN PRODUCT_DETAILS PD WHERE PD.STATUS=:status AND P.LANG=:locale AND PD.CODE=P.CODE", nativeQuery = true)
    List<Product> findByLangAndStatus(String locale, Integer status);

    @Query(value = "SELECT P.* FROM PRODUCT P JOIN PRODUCT_DETAILS PD WHERE PD.CODE=:code AND P.LANG=:locale AND PD.CODE=P.CODE", nativeQuery = true)
    Product findByCode(String locale, String code);

    @Query(value = "SELECT P.* FROM PRODUCT P JOIN PRODUCT_DETAILS PD WHERE PD.CODE=:code AND P.LANG=:locale AND PD.CODE=P.CODE AND PD.STATUS=:status", nativeQuery = true)
    Product findByCodeAndStatus(String locale, String code, Integer status);

    @Query(value = "SELECT P.* FROM PRODUCT P JOIN LOCALISED_STRINGS L WHERE P.NAME_ID=L.LOCALISED_ID AND L.STRINGS_KEY=:locale AND L.STRINGS LIKE %:name%", nativeQuery = true)
    List<Product> findByName(String locale, String name);

    @Query(value = "SELECT * FROM PRODUCT prod where prod.LANG<>:locale AND prod.CODE NOT in (SELECT prod1.CODE FROM PRODUCT prod1 where prod1.lang = :locale);", nativeQuery = true)
    List<Product> findMissingProducts(String locale);
}
