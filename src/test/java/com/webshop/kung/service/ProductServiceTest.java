package com.webshop.kung.service;

import com.webshop.kung.constants.Constants;
import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;
import com.webshop.kung.repository.ProductDetailsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

//    @Autowired
//    private ProductDetailsRepository productDetailsRepository;

    @Test
    public void getProductByCode() throws Exception {
        Category category = new Category(null, new Locale("EN"),  "beat");
        Category catAfter = categoryService.addCategory(category);
        Locale lang = new Locale("en");
        ProductDetails prodDet = new ProductDetails("A121", new BigDecimal(12.11), Constants.PER_UNIT, "photoLoc", Constants.PRODUCT_ACTIVE);
        Product prod = new Product(category, lang, "red beat", "nice for eating",prodDet);
 //       productDetailsRepository.save(prodDet);
        productService.addProduct(prod);
        Product p1 = productService.findByCode(lang,"A121");
        assertEquals(prod.getProductDetails().getCode(), p1.getProductDetails().getCode());
        assertEquals(prod.getName(), p1.getName());
//      Assert.assertTrue("List must be empty", catList.);
    }

    @Test
    public void getProductByLocaleName() throws Exception {
        Category category = new Category(null, new Locale("EN"), "wheat");
        Category catAfter = categoryService.addCategory(category);
        Locale lang = new Locale("en");
        ProductDetails prodDet = new ProductDetails("A135", new BigDecimal(12.11), Constants.PER_UNIT, "photoLoc", Constants.PRODUCT_ACTIVE);
        Product prodEn = new Product(category, lang, "red beat", "nice for eating",prodDet);
//        productDetailsRepository.save(prodDet);
        productService.addProduct(prodEn);
        Product prodNl = new Product(category, new Locale("NL"), "rode biet", "goed voor eten", prodDet);
        productService.addProduct(prodNl);
        ProductDetails prodDet2 = new ProductDetails("A136", new BigDecimal(11.25), Constants.PER_UNIT, "photoLoc", Constants.PRODUCT_ACTIVE);
        Product prod2 = new Product(category, lang, "red wheat", "not too much",prodDet2);
//        productDetailsRepository.save(prodDet2);
        productService.addProduct(prod2);
        Product prod2Nl = new Product(category, new Locale("NL"), "rode wiet", "niet te veel",prodDet2);

        productService.addProduct(prod2Nl);

        List<Product> prod1s = productService.findByLocaleName(new Locale("NL"), "rode wiet");
        assertFalse(prod1s.isEmpty());
        assertEquals(1, prod1s.size());
        Product p1 = prod1s.get(0);
        assertEquals(prod2Nl.getProductDetails().getCode(), p1.getProductDetails().getCode());
        assertEquals(prod2Nl.getName(), p1.getName());
        assertEquals(prod2Nl.getProductDetails().getPrice(), p1.getProductDetails().getPrice());
        prodDet2.setPrice(new BigDecimal("44.44"));
        productService.saveDetails(prodDet2);
//        productDetailsRepository.save(prodDet2);

        prod1s = productService.findByLocaleName(new Locale("NL"), "rode wiet");
        p1 = prod1s.get(0);
        assertEquals(new BigDecimal("44.44"), p1.getProductDetails().getPrice());
//      Assert.assertTrue("List must be empty", catList.);
//
    }

//    for (ProductDetails.PriceUnit pu : ProductDetails.PriceUnit.values()){
//            System.out.println("------------------- " + pu.name() + " --------------");
//        }

}
