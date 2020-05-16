package com.webshop.service;

import com.webshop.entity.Category;
import com.webshop.exception.CategoryAlreadyExistsException;
import com.webshop.exception.CategoryException;
import com.webshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

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
    public Category findCategory(String code, Locale lang) {
        return categoryRepository.findByCodeAndLang(code, lang);
    }

    @Override
    public void addCategory(Long id, Locale locale, String code, String name, String upperCode) throws CategoryAlreadyExistsException {
        Category category = null;
        if (id != null)
            category = findCategory(id);
        if (category == null)
            category = new Category();
        category.setLang(locale);
        category.setCode(code);
        category.setName(name);
        if (upperCode==null || "".equalsIgnoreCase(upperCode)) {
            category.setUpperCategory(null);
        } else {
            category.setUpperCategory(findCategory(upperCode, locale));
        }
        if (findCategory(category.getCode(), category.getLang())!=null && category.getId()==null){
            throw new CategoryAlreadyExistsException("Category already exist for code "+ code);
        }
        addCategory(category);
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getSelectedCategory(String catStr, Locale locale, Category origCat) {
        Category selectCat = origCat;
        if (catStr != null && !"ALL".equalsIgnoreCase(catStr)){
            List<Category> catList = findByName(locale, catStr);
            if (catList != null && !catList.isEmpty()) {
                Category cat = catList.get(0);
                return cat;
            }
        } else if ("ALL".equalsIgnoreCase(catStr)) {
            selectCat = new Category(null, locale, "ALL", "");
            List<Category> cats = findMainCategories(locale);
            for (Category categ : cats) {
                selectCat.addSubCategory(categ);
            }
        }
        return selectCat;

    }

    @Override
    public List<Category> getAllSubCategories(Category category) {
        return getAllSubCategories(category, null);
    }

    private List<Category> getAllSubCategories(Category selectedCategory, List<Category> categoryList) {
        if (categoryList==null)
            categoryList = new ArrayList<>();
        if (selectedCategory != null) {
            categoryList.add(selectedCategory);
            List<Category> cats = findSubCategories(selectedCategory);
            if (cats != null){
                for (Category cat : cats) {
                    getAllSubCategories(cat, categoryList);
                }
            }
        }
        return categoryList;
    }

    @Override
    @Cacheable("Category")
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
