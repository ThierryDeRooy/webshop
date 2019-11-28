package com.webshop.kung.service;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Test
    public void getEmptyList() throws Exception {
        Iterable<Category> catList = categoryService.listCategories(new Locale("FR"));
        assertEquals(0, StreamSupport.stream(catList.spliterator(), false).count());
//      Assert.assertTrue("List must be empty", catList.);
    }

    @Test
    public void addCategory() throws Exception {
        Category category = new Category(null, new Locale("EN"), "beans");
        Category catAfter = categoryService.addCategory(category);
//            Assert.assertFalse("Category id is NOT null ! " + category.getId(), category!=null);
        Assert.assertFalse("Category is null after saving!! ", catAfter==null);
        Iterable<Category> catList = categoryService.listCategories(new Locale("EN"));
        Assert.assertNotNull(catList);

        }

    @Test
    public void findProducts() {
        Category cat = categoryService.findCategory(3);
        assertFalse("Category is empty",cat == null);
        System.out.println("CATEGORY FOUND = " + cat.getFullName());
        List<Product> products = productService.findAllProductsByCategory(cat);
        assertEquals("Not the right number of Products", 2 , products.size());
        assertEquals("not the right product", "red beat", products.get(0).getName());
        assertEquals("not the right product", "B125", products.get(1).getProductDetails().getCode());
    }

    @Test
    public void findMainCategories() {
        List<Category> catList = categoryService.findMainCategories(new Locale("DE"));
        assertEquals("number of main categories", 2, catList.size());
        String[] expCats = { "level1_A", "level1_B"};
        for (int i=0; i < expCats.length; i++) {
            assertEquals(expCats[i], catList.get(i).getName());
        }
    }

    @Test
    public void findCategoryTree(){
        List<Category> catList = categoryService.findMainCategories(new Locale("DE"));
        System.out.println("------------- " + catList.get(0).getFullName());
        List<Category> cat1List = catList.get(0).getSubCategories();
        assertEquals("number of sub categoriesof cat 0", 1, cat1List.size());
        String[] expCats = {"level1_A/level2_A"};
        for (int i=0; i < expCats.length; i++) {
            assertEquals(expCats[i], cat1List.get(i).getFullName());
        }
    }

    @Test
    public void findLowestLevelCats(){
        Locale langTest = new Locale("DE");
        Category category = new Category(null, langTest, "level1_A");
        Category catAfter = categoryService.addCategory(category);
        Category category2 = new Category(null, langTest,"level1_B");
        catAfter = categoryService.addCategory(category2);
        category = new Category(category, langTest, "level2_A");

        List<Category> upperCats = categoryService.findByName(langTest, "level1_A");
        Assert.assertEquals(1, upperCats.size());
        Category upperCat = upperCats.get(0);
        System.out.println("--- upper CATEGORY: " + upperCat.getId()+ " --> " + upperCat.getName()+ "  MainCat = " + upperCat.getId());
        category.setUpperCategory(upperCat);
        System.out.println("--- CATEGORY: " + category.getId()+ " --> " + category.getName()+ "  MainCat = " + category.getUpperCategory().getId());
        catAfter = categoryService.addCategory(category);

        List<Category> cats = categoryService.findLowestLevelCategories(langTest);
        for (Category c : cats) {
            System.out.println("--- CATEGORY: " + c.getId()+ " --> " + c.getName()+ "  MainCat = " + c.getId() + " fullName = " + c.getFullName());
        }
        Assert.assertEquals(2, cats.size());
        Assert.assertEquals("level1_B", cats.get(0).getName());
        Assert.assertEquals("level2_A", cats.get(1).getName());
    }
}