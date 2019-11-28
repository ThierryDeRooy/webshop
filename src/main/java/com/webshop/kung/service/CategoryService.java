package com.webshop.kung.service;

import com.webshop.kung.entity.Category;

import java.util.List;
import java.util.Locale;

public interface CategoryService {
    Iterable<Category> listCategories(Locale lang);
    Category findCategory(long id);
    Category addCategory(Category category);
    List<Category> findSubCategories(Category category);
    List<Category> findMainCategories(Locale lang);
    List<Category> findLowestLevelCategories(Locale lang);
    List<Category> findByName(Locale locale, String name);
}
