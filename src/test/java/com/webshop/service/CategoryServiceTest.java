package com.webshop.service;

import com.webshop.entity.Category;
import com.webshop.exception.CategoryAlreadyExistsException;
import com.webshop.repository.CategoryRepository;
import com.webshop.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Before
    public void init() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void getEmptyList() throws Exception {
        Iterable<Category> catList = categoryService.listCategories(new Locale("FR"));
        assertEquals(0, StreamSupport.stream(catList.spliterator(), false).count());
//      Assert.assertTrue("List must be empty", catList.);
    }

    @Test
    public void addCategory() throws Exception {
        Category category = new Category(null, new Locale("EN"), "NewCategory" , "NC");
        Category catAfter = categoryService.addCategory(category);
//            Assert.assertFalse("Category id is NOT null ! " + category.getId(), category!=null);
        Assert.assertFalse("Category is null after saving!! ", catAfter==null);
        Assert.assertEquals(category, catAfter);
        Iterable<Category> catList = categoryService.listCategories(new Locale("EN"));
        Assert.assertNotNull(catList);
        Iterator<Category> it = catList.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Category cat = it.next();
            if (category.getName().equals(cat.getName()))
                found = true;
        }
        Assert.assertTrue("new added category NOT found in list", found);
    }

    @Test
    public void addNewCategory() {
        categoryService.addCategory(null, new Locale("EN"),"T001","NewProductName",null);
        Iterable<Category> catList = categoryService.listCategories(new Locale("EN"));
        Assert.assertNotNull(catList);
        Iterator<Category> it = catList.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Category cat = it.next();
            if ("NewProductName".equals(cat.getName()))
                found = true;
        }
        Assert.assertTrue("new added category NOT found in list", found);
    }

    @Test
    public void addCategoryLang() {
        createCategories();
        categoryService.addCategory(null, new Locale("NL"),"NC1","CatNederlands",null);
        Iterable<Category> catList = categoryService.listCategories(new Locale("NL"));
        Assert.assertNotNull(catList);
        Iterator<Category> it = catList.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Category cat = it.next();
            if ("CatNederlands".equals(cat.getName()))
                found = true;
        }
        Assert.assertTrue("new added category NOT found in list", found);
    }

    @Test(expected = CategoryAlreadyExistsException.class)
    public void addCategoryExistingCode() {
        createCategories();
        categoryService.addCategory(null, new Locale("EN"), "NC1", "catEnglish", null);
    }

    @Test
    public void addCategoryNonExistingUpperCat() {
        createCategories();
        categoryService.addCategory(null, new Locale("EN"), "NC5", "catEnglish", "NOT");
        Iterable<Category> catList = categoryService.listCategories(new Locale("EN"));
        Assert.assertNotNull(catList);
        Iterator<Category> it = catList.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Category cat = it.next();
            if ("catEnglish".equals(cat.getName()))
                found = true;
        }
        Assert.assertTrue("new added category NOT found in list", found);

    }

    @Test
    public void findCategoryById() {
        createCategories();
        Category category = new Category(null, new Locale("EN"), "NewCategory" , "NC");
        Category catAfter = categoryService.addCategory(category);
        Long id = catAfter.getId();

        Category cat = categoryService.findCategory(id);
        assertFalse("Category is empty",cat == null);
        System.out.println("CATEGORY FOUND = " + cat.getFullName());
        assertEquals("category NOT found bu ID", id, cat.getId());
    }

    @Test
    public void findMainCategories() {
        createCategories();
        List<Category> catList = categoryService.findMainCategories(new Locale("EN"));
        assertEquals("number of main categories", 3, catList.size());
        String[] catListNames = new String[catList.size()];
        for (int i=0; i<catList.size(); i++)
            catListNames[i] = catList.get(i).getName();
        String[] expCats = { "NewCategory1", "NewCategory2", "NewCategory3"};
        assertArrayEquals("Main category is NOT in the expectations", expCats, catListNames);
//        for (int i=0; i < expCats.length; i++) {
//            Assert.assertTrue("The main category doesn't contain " + expCats[i],!catList.contains(expCats[i]));
//        }
    }

    @Test
    public void findCategoryTree(){
        createCategories();
        List<Category> catList = categoryService.findMainCategories(new Locale("EN"));
        System.out.println("------------- " + catList.get(0).getFullName());
        List<Category> cat1List = catList.get(0).getSubCategories();
        assertEquals("number of sub categoriesof cat 0", 2, cat1List.size());
        String[] catListNames = new String[cat1List.size()];
        String[] catListFullNames = new String[cat1List.size()];
        for (int i=0; i<cat1List.size(); i++) {
            catListNames[i] = cat1List.get(i).getName();
            catListFullNames[i] = cat1List.get(i).getFullName();
        }
        String[] expCats = {"NewCategory11", "NewCategory12"};
        String[] expCatsFullName = {"NewCategory1/NewCategory11", "NewCategory1/NewCategory12"};
        assertArrayEquals("Sub category is NOT in the expectations", expCats, catListNames);
        assertArrayEquals("Full name sub category is NOT in the expectations", expCatsFullName, catListFullNames);
//        for (int i=0; i < expCats.length; i++) {
//            Assert.assertTrue("subcat list doesn't contain "+expCats[i], cat1List.contains(expCats[i]));
//            Assert.assertEquals("full name not equals "+expCatsFullName[i], expCatsFullName[i], cat1List.get(i).getFullName());
//        }
    }

    @Test
    public void findLowestLevelCats(){
        Locale langTest = new Locale("DE");
        Category category = new Category(null, langTest, "level1_A", "1A");
        Category catAfter = categoryService.addCategory(category);
        Category category2 = new Category(null, langTest,"level1_B", "1B");
        catAfter = categoryService.addCategory(category2);
        category = new Category(category, langTest, "level2_A", "2A");
        catAfter = categoryService.addCategory(category);

        List<Category> upperCats = categoryService.findByName(langTest, "level1_A");
        Assert.assertEquals(1, upperCats.size());
        Category upperCat = upperCats.get(0);
        System.out.println("--- upper CATEGORY: " + upperCat.getId()+ " --> " + upperCat.getName()+ "  MainCat = " + upperCat.getId());
        category.setUpperCategory(upperCat);
        System.out.println("--- CATEGORY: " + category.getId()+ " --> " + category.getName()+ "  MainCat = " + category.getUpperCategory().getId());

        List<Category> cats = categoryService.findLowestLevelCategories(langTest);
        for (Category c : cats) {
            System.out.println("--- CATEGORY: " + c.getId()+ " --> " + c.getName()+ "  MainCat = " + c.getId() + " fullName = " + c.getFullName());
        }
        Assert.assertEquals(2, cats.size());
        Assert.assertEquals("level1_B", cats.get(0).getName());
        Assert.assertEquals("level2_A", cats.get(1).getName());
    }

    private void createCategories() {
        Category cat1  = new Category(null, new Locale("EN"), "NewCategory1" , "NC1");
        Category cat2  = new Category(null, new Locale("EN"), "NewCategory2" , "NC2");
        Category cat11  = new Category(cat1, new Locale("EN"), "NewCategory11" , "NC11");
        Category cat12  = new Category(cat1, new Locale("EN"), "NewCategory12" , "NC12");
        Category cat3  = new Category(null, new Locale("EN"), "NewCategory3" , "NC3");
        Category cat21  = new Category(cat2, new Locale("EN"), "NewCategory21" , "NC21");
        categoryService.addCategory(cat1);
        categoryService.addCategory(cat2);
        categoryService.addCategory(cat11);
        categoryService.addCategory(cat12);
        categoryService.addCategory(cat3);
        categoryService.addCategory(cat21);

    }
}