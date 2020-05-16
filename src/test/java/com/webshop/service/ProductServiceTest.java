package com.webshop.service;

import com.webshop.constants.Constants;
import com.webshop.entity.Category;
import com.webshop.entity.Product;
import com.webshop.entity.ProductDetails;
import com.webshop.exception.ProductException;

import com.webshop.repository.CategoryRepository;
import com.webshop.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

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


//    @Autowired
//    private ProductDetailsRepository productDetailsRepository;

    @Test
    public void addNewProduct() throws IOException {
        Locale lang = new Locale("fr");
        ProductDetails prodDet = new ProductDetails("0001", new BigDecimal("12.11"), new BigDecimal("9.50"), Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod = new Product(null, lang, "red beat", "nice for eating",prodDet);
        productService.updateProduct(lang, prod, null, 0);
        Product prodFromDb = productService.findByCode(lang,"0001" );
         productsAreEqual(prod, prodFromDb);
//        Assert.assertTrue(EqualsBuilder.reflectionEquals(prod, prodFromDb));
    }

    @Test
    public void addProductOtherLang() throws IOException {
        Locale lang = new Locale("ch");
        ProductDetails prodDet = new ProductDetails("0002", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod = new Product(null, lang, "red beat", "nice for eating",prodDet);
        productService.updateProduct(lang, prod, null, 0);
        Product prodFromDb = productService.findByCode(lang,"0002" );
        productsAreEqual(prod, prodFromDb);
//        Assert.assertTrue(EqualsBuilder.reflectionEquals(prod, prodFromDb));
        Locale lang2 = new Locale("nl");
        Product prod2 = new Product(null, lang2, "titleNL", "NL description",prodDet);
        productService.updateProduct(lang2, prod2, null, 0);
        Product prodFromDb2 = productService.findByCode(lang2,"0002" );
        productsAreEqual(prod2, prodFromDb2);
    }

    @Test
    public void modifyProduct() throws IOException {
        Locale lang = new Locale("ch");
        ProductDetails prodDet = new ProductDetails("0002", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod = new Product(null, lang, "red beat", "nice for eating",prodDet);
        productService.updateProduct(lang, prod, null, 0);
        Product prodFromDb = productService.findByCode(lang,"0002" );
        productsAreEqual(prod, prodFromDb);
        // modify Product only
        Product prod2 = new Product(null, lang, "title2", "description2",prodDet);
        productService.updateProduct(lang, prod2, null, 2);
        prod2.getProductDetails().setStock(2);
        Product prodFromDb2 = productService.findByCode(lang,"0002" );
        productsAreEqual(prod2, prodFromDb2);
        // modify productDetails
        ProductDetails prodDet2 = new ProductDetails("0002", new BigDecimal("20.11"), new BigDecimal("9.00"),Constants.PER_WEIGHT_KG, "photoLoc2", 0, new BigDecimal("1.00"));
        prodDet2.setId(prodDet.getId());
        prod2.setProductDetails(prodDet2);
        productService.updateProduct(lang, prod2, null, 2);
        prodFromDb2 = productService.findByCode(lang,"0002" );
        productsAreEqual(prod2, prodFromDb2);
    }

    @Test(expected = ProductException.class)
    public void addSameProduct() throws IOException {
        Locale lang = new Locale("ch");
        ProductDetails prodDet = new ProductDetails("0002", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod = new Product(null, lang, "red beat", "nice for eating",prodDet);
        productService.updateProduct(lang, prod, null, 0);
        Product prodFromDb = productService.findByCode(lang,"0002" );
        productsAreEqual(prod, prodFromDb);
//        Assert.assertTrue(EqualsBuilder.reflectionEquals(prod, prodFromDb));
        Locale lang2 = new Locale("ch");
        ProductDetails prodDet2 = new ProductDetails("0002", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod2 = new Product(null, lang2, "title2", "description2",prodDet2);
        productService.updateProduct(lang2, prod2, null, 0);
        Product prodFromDb2 = productService.findByCode(lang2,"0002" );
        productsAreEqual(prod2, prodFromDb2);
    }

    @Test(expected = ProductException.class)
    public void addSameProduct2() throws IOException {
        Locale lang = new Locale("ch");
        ProductDetails prodDet = new ProductDetails("0002", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        Product prod = new Product(null, lang, "red beat", "nice for eating",prodDet);
        productService.updateProduct(lang, prod, null, 0);
        Product prodFromDb = productService.findByCode(lang,"0002" );
        productsAreEqual(prod, prodFromDb);
//        Assert.assertTrue(EqualsBuilder.reflectionEquals(prod, prodFromDb));
        Locale lang2 = new Locale("ch");
        ProductDetails prodDet2 = new ProductDetails("0003", new BigDecimal("12.11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc", 0, new BigDecimal("0.15"));
        prodDet2.setId(11111L);
        Product prod2 = new Product(null, lang2, "title2", "description2",prodDet2);
        productService.updateProduct(lang2, prod2, null, 0);
        Product prodFromDb2 = productService.findByCode(lang2,"0002" );
        productsAreEqual(prod2, prodFromDb2);
    }

    @Test
    public void getAllProductsSameLang() throws IOException {
        Locale lang = new Locale("en");
        ProductDetails[] productDetails = new ProductDetails[10];
        Product[] products = new Product[10];
        for (int i=0; i< productDetails.length; i++) {
            productDetails[i] = new ProductDetails("C0" + i, new BigDecimal("1"+i+".11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc"+i, 0, new BigDecimal("1.00"));
            products[i] = new Product(null, lang, "Title"+i, "description"+i, productDetails[i]);
            productService.updateProduct(lang, products[i], null, 0);
        }
        List<Product> productList = productService.findAllProducts(lang);
        Iterator<Product> it = productList.iterator();
        int ctr = 0;
        while (it.hasNext()) {
            productsAreEqual(products[ctr++], it.next());
        }

    }

    @Test
    public void getAllProducts() throws IOException {
        Locale lang = new Locale("en");
        Locale lang2 = new Locale("nl");
        Locale lang3 = new Locale("fr");
        ProductDetails[] productDetails = new ProductDetails[20];
        Product[] products = new Product[20];
        for (int i=0; i< 5; i++) {
            productDetails[i] = new ProductDetails("C0" + i, new BigDecimal("1"+i+".11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc"+i, 0, new BigDecimal("1.00"));
            products[i] = new Product(null, lang, "Title"+i, "description"+i, productDetails[i]);
            productService.updateProduct(lang, products[i], null, 10);
        }
        for (int i=5; i< 10; i++) {
            productDetails[i] = new ProductDetails("C0" + i, new BigDecimal("1"+i+".11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc"+i, 0, new BigDecimal("1.00"));
            products[i] = new Product(null, lang, "Title"+i, "description"+i, productDetails[i]);
            productService.updateProduct(lang, products[i], null, 10);
            productService.updateProduct(lang2, new Product(null, lang2, "TitleNL"+i, "descriptionNL"+i, productDetails[i]), null, 0);
        }
        for (int i=10; i< 15; i++) {
            productDetails[i] = new ProductDetails("C0" + i, new BigDecimal("1"+i+".11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc"+i, 0, new BigDecimal("1.00"));
            products[i] = new Product(null, lang3, "TitleFR"+i, "descriptionFR"+i, productDetails[i]);
            productService.updateProduct(lang3, products[i], null, 10);
            productService.updateProduct(lang2, new Product(null, lang2, "TitleNL"+i, "descriptionNL"+i, productDetails[i]), null, 0);
        }
        for (int i=15; i< productDetails.length; i++) {
            productDetails[i] = new ProductDetails("C0" + i, new BigDecimal("1"+i+".11"), new BigDecimal("9.50"),Constants.PER_UNIT, "photoLoc"+i, 0, new BigDecimal("1.00"));
            products[i] = new Product(null, lang3, "TitleFR"+i, "descriptionFR"+i, productDetails[i]);
            productService.updateProduct(lang3, products[i], null, 10);
        }
        List<Product> productList = productService.findAllProducts(lang);
        Assert.assertEquals("number of Products", 10, productList.size());
        Iterator<Product> it = productList.iterator();
        int ctr = 0;
        while (it.hasNext()) {
            productsAreEqual(products[ctr++], it.next());
        }

        productList = productService.findMissingProducts(lang);
        Assert.assertEquals("number of Products", 15, productList.size());
        ctr = 0;
        while (it.hasNext()) {
            productsAreEqual(products[10+ctr++], it.next());
        }

        Page<Product> pagedResult = productService.getAllProductsByStock(lang, 0, 0, 100, "productDetails.code", "ASC", null, null);
        productList = pagedResult.getContent();
        Assert.assertEquals("number of Products", 10, productList.size());
        it = productList.iterator();
        ctr = 0;
        while (it.hasNext()) {
            productsAreEqual(products, it.next());
        }

        pagedResult = productService.getAllProductsByStock(lang2, 0, 0, 100, "productDetails.code", "ASC", null, null);
        productList = pagedResult.getContent();
        Assert.assertEquals("number of Products", 15, productList.size());


        pagedResult = productService.getAllProductsByStock(lang3, 0, 0, 100, "productDetails.code", "ASC", null, null);
        productList = pagedResult.getContent();
        Assert.assertEquals("number of Products", 20, productList.size());
        it = productList.iterator();
        ctr = 0;
        while (it.hasNext()) {
            productsAreEqual(products, it.next());
        }


    }


    @Test
    public void getProductByCode() throws Exception {
        Category category = new Category(null, new Locale("EN"),  "beat", "beat");
        Category catAfter = categoryService.addCategory(category);
        Locale lang = new Locale("en");
        ProductDetails prodDet = new ProductDetails("A121", new BigDecimal(12.11), new BigDecimal(21),Constants.PER_UNIT, "photoLoc", Constants.PRODUCT_ACTIVE, new BigDecimal(0.15));
        Product prod = new Product(category, lang, "red beat", "nice for eating",prodDet);
 //       productDetailsRepository.save(prodDet);
        productService.addProduct(prod);
        Product p1 = productService.findByCode(lang,"A121");
        assertEquals(prod.getProductDetails().getCode(), p1.getProductDetails().getCode());
        assertEquals(prod.getName(), p1.getName());
//      Assert.assertTrue("List must be empty", catList.);
    }


//    for (ProductDetails.PriceUnit pu : ProductDetails.PriceUnit.values()){
//            System.out.println("------------------- " + pu.name() + " --------------");
//        }

    private void productsAreEqual(Product prod1, Product prod2) {
        Assert.assertEquals(prod1.getName(), prod2.getName());
        Assert.assertEquals(prod1.getId(), prod2.getId());
        Assert.assertEquals(prod1.getDescription(), prod2.getDescription());
        Assert.assertEquals(prod1.getLang().getLanguage(), prod2.getLang().getLanguage());
        Assert.assertEquals(prod1.getProductDetails().getId(), prod2.getProductDetails().getId());
        Assert.assertEquals(prod1.getProductDetails().getCode(), prod2.getProductDetails().getCode());
        Assert.assertEquals(prod1.getProductDetails().getPhotoLoc(), prod2.getProductDetails().getPhotoLoc());
        Assert.assertEquals(prod1.getProductDetails().getPrice(), prod2.getProductDetails().getPrice());
        Assert.assertEquals(prod1.getProductDetails().getBtw(), prod2.getProductDetails().getBtw());
        Assert.assertEquals(prod1.getProductDetails().getPriceInclBtw(), prod2.getProductDetails().getPriceInclBtw());
        Assert.assertEquals(prod1.getProductDetails().getTransportPoints(), prod2.getProductDetails().getTransportPoints());
        Assert.assertEquals(prod1.getProductDetails().getStock(), prod2.getProductDetails().getStock());
        Assert.assertEquals(prod1.getProductDetails().getUnit(), prod2.getProductDetails().getUnit());
    }
    private void productsAreEqual(Product[] prods1, Product prod2) {
        int ctr= prods1.length-1;
        while (ctr>=0 && prods1[ctr].getId()!=prod2.getId())
            ctr--;
        if (ctr>=0) {
            Product prod1 = prods1[ctr];
            Assert.assertEquals(prod1.getName(), prod2.getName());
            Assert.assertEquals(prod1.getId(), prod2.getId());
            Assert.assertEquals(prod1.getDescription(), prod2.getDescription());
            Assert.assertEquals(prod1.getLang().getLanguage(), prod2.getLang().getLanguage());
            Assert.assertEquals(prod1.getProductDetails().getId(), prod2.getProductDetails().getId());
            Assert.assertEquals(prod1.getProductDetails().getCode(), prod2.getProductDetails().getCode());
            Assert.assertEquals(prod1.getProductDetails().getPhotoLoc(), prod2.getProductDetails().getPhotoLoc());
            Assert.assertEquals(prod1.getProductDetails().getPrice(), prod2.getProductDetails().getPrice());
            Assert.assertEquals(prod1.getProductDetails().getBtw(), prod2.getProductDetails().getBtw());
            Assert.assertEquals(prod1.getProductDetails().getPriceInclBtw(), prod2.getProductDetails().getPriceInclBtw());
            Assert.assertEquals(prod1.getProductDetails().getTransportPoints(), prod2.getProductDetails().getTransportPoints());
            Assert.assertEquals(prod1.getProductDetails().getStock(), prod2.getProductDetails().getStock());
            Assert.assertEquals(prod1.getProductDetails().getUnit(), prod2.getProductDetails().getUnit());
        } else {
            Assert.assertTrue("Product NOT found: " + prod2.getName(), false);
        }
    }
}
