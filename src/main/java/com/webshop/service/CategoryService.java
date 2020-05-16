package com.webshop.service;

import com.webshop.entity.Category;
import com.webshop.exception.CategoryAlreadyExistsException;

import java.util.List;
import java.util.Locale;

public interface CategoryService {
    List<Category> findAll();
    Iterable<Category> listCategories(Locale lang);
    Category findCategory(long id);
    Category findCategory(String code, Locale lang);
    void addCategory(Long id, Locale locale, String code, String name, String upperCode) throws CategoryAlreadyExistsException;
    Category addCategory(Category category);
    Category getSelectedCategory(String category, Locale lang, Category origCat);
    List<Category> getAllSubCategories(Category category);
    List<Category> findSubCategories(Category category);
    List<Category> findMainCategories(Locale lang);
    List<Category> findLowestLevelCategories(Locale lang);
    List<Category> findByName(Locale locale, String name);
}
