package com.webshop.repository;

import com.webshop.entity.Category;
import com.webshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

//     List<Product> findByCategory(Category category);
//    Product findByCode(String code);
//    List<Product> findByLangAndName(Locale lang, String name);
    List<Product> findByLang(Locale lang);

    Optional<Product> findById(Long id);

    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale")
    Optional<Product> findByLangAndCode(Locale locale, String code);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.category=:category")
//    List<Product> findByCategoryAndStock(Category category, int stock);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.category IN :categoryList")
//    List<Product> findByCategoriesAndStock(List<Category> categoryList, int stock);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.category IN :categoryList")
//    Page<Product> findByCategoriesAndStock(List<Category> categoryList, int stock, Pageable pageable);
    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.category.code IN :catCodeList " +
            "AND (P.lang=:locale OR (P.lang=:defaultLoc AND :locale NOT IN (SELECT P2.lang FROM Product P2 WHERE P2.productDetails.code=P.productDetails.code)))")
    Page<Product> findByCategoriesAndStock(List<String> catCodeList, Locale locale, Locale defaultLoc, int stock, Pageable pageable);

    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.category IN :categoryList AND (LOWER(P.name) like %:search% OR LOWER(P.description) like %:search% OR LOWER(P.productDetails.code) like %:search%)")
    Page<Product> searchByCategoriesAndStock(List<Category> categoryList, int stock, String search, Pageable pageable);


    //    @Query(value = "SELECT P.* FROM PRODUCT P, PRODUCT_DETAILS PD WHERE PD.status=:status AND P.lang=:locale AND PD.code=P.code", nativeQuery = true)
//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.lang=:locale")
//    List<Product> findByLangAndStock(Locale locale, int stock);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND P.lang=:locale")
    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND (P.lang=:locale OR (P.lang=:defaultLoc AND :locale NOT IN (SELECT P2.lang FROM Product P2 WHERE P2.productDetails.code=P.productDetails.code)))")
    Page<Product> findByLangAndStock(Locale locale, Locale defaultLoc, int stock, Pageable pageable);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND (LOWER(P.name) like %:search% OR LOWER(P.description) like %:search% OR LOWER(P.productDetails.code) like %:search%) AND P.lang=:locale")
    @Query("SELECT P FROM Product P WHERE P.productDetails.stock>:stock AND (LOWER(P.name) like %:search% OR LOWER(P.description) like %:search% OR LOWER(P.productDetails.code) like %:search%) " +
            "AND (P.lang=:locale OR (P.lang=:defaultLoc AND :locale NOT IN (SELECT P2.lang FROM Product P2 WHERE P2.productDetails.code=P.productDetails.code)))")
    Page<Product> searchByLangAndStock(Locale locale, Locale defaultLoc, int stock, String search, Pageable pageable);

//    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale")
    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND (P.lang=:locale OR :locale NOT IN (SELECT P2.lang FROM Product P2 WHERE P2.productDetails.code=P.productDetails.code))")
    Product findByCode(Locale locale, String code);

    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale")
    Product findByCodeAndLang(Locale locale, String code);

    //    @Query(value = "SELECT P.* FROM PRODUCT P, PRODUCT_DETAILS PD WHERE PD.code=:code AND P.lang=:locale AND PD.code=P.code AND PD.status=:status", nativeQuery = true)
//    @Query("SELECT P FROM Product P WHERE P.productDetails.code=:code AND P.lang=:locale AND P.productDetails.stock>:stock")
//    Product findByCodeAndStock(Locale locale, String code, int stock);

//    @Query(value = "SELECT P.* FROM PRODUCT P, LOCALISED_STRINGS L WHERE P.NAME_ID=L.LOCALISED_ID AND L.STRINGS_KEY=:locale AND L.STRINGS LIKE %:name%", nativeQuery = true)
//    List<Product> findByName(String locale, String name);

//    @Query(value = "SELECT * FROM PRODUCT prod where prod.LANG<>:locale AND prod.code NOT in (SELECT prod1.code FROM PRODUCT prod1 where prod1.lang = :locale);", nativeQuery = true)
    @Query("SELECT prod FROM Product prod where prod.lang<>:locale AND prod.productDetails.code NOT in (SELECT prod1.productDetails.code FROM Product prod1 where prod1.lang=:locale)")
    List<Product> findMissingProducts(Locale locale);

    @Query("SELECT P FROM Product P, OrderDetails OD, ProductDetails PD, Product P2 WHERE OD.product=P2 AND P2.productDetails=PD AND P.productDetails=PD AND P.lang=:locale AND P.productDetails.stock>:stock GROUP BY P ORDER BY SUM (OD.quantity)")
    Page<Product> ListByPopularityAndLangAndStock(Locale locale, int stock, Pageable pageable);
}

