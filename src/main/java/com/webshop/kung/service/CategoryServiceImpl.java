package com.webshop.kung.service;

import com.webshop.kung.entity.Category;
import com.webshop.kung.exception.CategoryException;
import com.webshop.kung.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Iterable<Category> listCategories(Locale lang) {
        return categoryRepository.findByLang(lang);
    }

    @Override
    public Category findCategory(long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent())
            return optionalCategory.get();
        else
            throw new CategoryException("Category Not Found");
     }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findSubCategories(Category category) {
        return categoryRepository.findByUpperCategory(category);
    }

    @Override
    public List<Category> findMainCategories(Locale lang) {
        Iterable<Category> categories = categoryRepository.findByLang(lang);
        List<Category> mainCategories = new ArrayList<Category>();
        Iterator<Category> it = categories.iterator();
        while (it.hasNext()) {
            Category newCategory = it.next();
            if (newCategory.getUpperCategory() != null) {
                if (!newCategory.getUpperCategory().getSubCategories().contains(newCategory))
                    newCategory.getUpperCategory().addSubCategory(newCategory);
            } else {
                mainCategories.add(newCategory);
            }
        }
 //        return findSubCategories(0L);
        return mainCategories;
    }

    @Override
    public List<Category> findLowestLevelCategories(Locale lang) {
        Iterable<Category> categories = categoryRepository.findByLang(lang);
        List<Category> categoryList = new ArrayList<Category>();
        Iterator<Category> it = categories.iterator();
        while (it.hasNext()) {
            Category newCategory = it.next();
            if (findSubCategories(newCategory).isEmpty())
                categoryList.add(newCategory);
        }

        return categoryList;
    }

    @Override
    public List<Category> findByName(Locale locale, String name) {
        return categoryRepository.findByLangAndName(locale, name);
    }


}
