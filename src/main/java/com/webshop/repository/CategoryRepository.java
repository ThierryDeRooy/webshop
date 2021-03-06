package com.webshop.repository;

import com.webshop.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Locale;

public interface CategoryRepository extends CrudRepository<Category, Long> {

//    @Query(value = "SELECT C.* FROM CATEGORY C JOIN LOCALISED_STRINGS L WHERE C.NAME_ID=L.LOCALISED_ID AND L.STRINGS_KEY=:locale AND L.STRINGS=:name", nativeQuery = true)
//    List<Category> findByName(@Param("locale") String locale, @Param("name") String name);

    List<Category> findByLang(Locale lang);
    List<Category> findByLangAndName(Locale lang, String name);
    List<Category> findByUpperCategory(Category category);
    Category findByCodeAndLang(String code, Locale lang);
}
