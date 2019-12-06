package com.webshop.kung.controller;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Order;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;
import com.webshop.kung.model.ProductInfo;
import com.webshop.kung.service.CategoryService;
import com.webshop.kung.service.OrderService;
import com.webshop.kung.service.ProductService;
import com.webshop.kung.validator.FileValidator;
import com.webshop.kung.validator.PhotoFileValidator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.CacheManager;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class AdminWebController {

//    private String UPLOADED_FOLDER = "D:\\webshop\\ShoppingCart-master\\kung\\";
//    private String SUB_FOLDER = "images\\";
//    private String UPLOADED_FOLDER = "D:/webshop/ShoppingCart-master/kung/";
    private String SUB_FOLDER = "images/";

//    @Autowired
//    private FileValidator fileValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
//        dataBinder.setValidator(fileValidator);
    }


    private CategoryService categoryService;
    private ProductService productService;
    private OrderService orderService;
//    private Locale lang = new Locale("EN");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    private CacheManager cacheManager;   // autowire cache manager


    @GetMapping("/addNewProduct")
    public String newProduct(Model model) {
//        model.addAttribute("categories", categoryService.findLowestLevelCategories());
        Locale loc = LocaleContextHolder.getLocale();
        model.addAttribute("categorien", getOrderCats());
        model.addAttribute("productInfo", new ProductInfo());
        model.addAttribute("lang", loc);
//        model.addAttribute("PriceUnits", ProductDetails.PriceUnit.values());
        List<Category> cats = categoryService.findMainCategories(loc);
        model.addAttribute("categories", cats);
        List<Product> productList = productService.findAllProducts(loc);
        model.addAttribute("products", productList);
        List<Product> missingProductList = productService.findMissingProducts(loc);
        model.addAttribute("missingProducts", missingProductList);

        return "addNewProduct";
    }

    @GetMapping("/addNewCategory")
    public String newCategory(HttpServletRequest request, Model model) {
//        model.addAttribute("categories", categoryService.findMainCategories(lang));
        request.getSession().setAttribute("categories", categoryService.findMainCategories(LocaleContextHolder.getLocale()));
        model.addAttribute("categorien", getOrderCats());
        model.addAttribute("category", new Category());
        return "addNewCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result, HttpServletRequest request,  Model model) {
        category.setLang(LocaleContextHolder.getLocale());
        categoryService.addCategory(category);
        return newCategory(request, model);
    }

    @GetMapping("/saveProduct")
    public String saveProduct(Model model) {
        return "redirect:/addNewProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("productInfo") ProductInfo productInfo,
//                              @Valid @RequestParam("file") MultipartFile file ,
                              BindingResult result,
                              @RequestParam("btnSubmit") String submit,
                              Model model,
                              HttpServletRequest request) {
        Locale loc = LocaleContextHolder.getLocale();
        if (!result.hasErrors()) {
            Product prod = productService.findByCode(loc, productInfo.getProduct().getProductDetails().getCode());
            if ("Add Product".equalsIgnoreCase(submit) && prod != null) {
                result.rejectValue("product.productDetails.code", "lang.codeAlreadyExists");
            }
            if (!productInfo.getFile().isEmpty()) {
                try {
                    copyFile(productInfo.getFile());
                } catch (IOException e) {
                    System.out.println("==================================="+ e.getMessage());
                    result.rejectValue("file", "lang.error.fileRejected");
                }
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("categorien", getOrderCats());
//            model.addAttribute("PriceUnits", ProductDetails.PriceUnit.values());
            List<Category> cats = categoryService.findMainCategories(LocaleContextHolder.getLocale());
            model.addAttribute("categories", cats);
            List<Product> productList = productService.findAllProducts(loc);
            model.addAttribute("products", productList);
            List<Product> missingProductList = productService.findMissingProducts(loc);
            model.addAttribute("missingProducts", missingProductList);
            return "addNewProduct";

        }


        Product product = checkProduct(productInfo.getProduct(), productInfo.getFile());

        productService.addProduct(product);
        clearCache();
        return "redirect:/addNewProduct";
    }



    @RequestMapping("/showOrders")
    public String showOrders(Model model,
                             HttpServletRequest request) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        String chosenOrderId = request.getParameter("id");
        if (chosenOrderId!=null && !chosenOrderId.isEmpty() && chosenOrderId.matches("\\d*")) {
            Long idLong = Long.parseLong(chosenOrderId);
            Order chosenOrder = orderService.findById(idLong);
            model.addAttribute("chosenOrder", chosenOrder);
        }
        return "orderTable";
    }

    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute("chosenOrder") Order order) {
        Order myOrder = orderService.findById(order.getId());
        myOrder.setStatus(order.getStatus());
        orderService.addOrder(myOrder);
        return "redirect:/showOrders";
    }


    private Map<Long ,String>  getOrderCats() {
        List<Category> cats = categoryService.findMainCategories(LocaleContextHolder.getLocale());
        Map<Long, String> map = new LinkedHashMap<Long, String>();
        return getOrderCatsSubRoutine("", map, cats);

    }
    private Map<Long, String> getOrderCatsSubRoutine(String addition, Map<Long, String> map, List<Category> cats){
        for (Category cat : cats){
            map.put(cat.getId(), addition + cat.getFullName());
            if (!cat.getSubCategories().isEmpty()) {
                getOrderCatsSubRoutine(addition , map, cat.getSubCategories());
            }
        }
        return map;
    }

    private Product checkProduct(Product product, MultipartFile file) {
        ProductDetails pd = productService.findByCode(product.getProductDetails().getCode());
        if (pd!=null) {
            if (product.getProductDetails().getPhotoLoc() == null) {
                product.getProductDetails().setPhotoLoc(pd.getPhotoLoc());
                product.getProductDetails().setId(pd.getId());
            }
        }
        Product prodExist = productService.findByCode(product.getLang(),product.getProductDetails().getCode());
        if (prodExist!=null) {
            prodExist.setDescription(product.getDescription());
            prodExist.setName(product.getName());
            prodExist.setCategory(product.getCategory());
            prodExist.getProductDetails().setUnit(product.getProductDetails().getUnit());
            prodExist.getProductDetails().setPrice(product.getProductDetails().getPrice());
            prodExist.getProductDetails().setPhotoLoc(product.getProductDetails().getPhotoLoc());
            prodExist.getProductDetails().setStatus(product.getProductDetails().getStatus());
            if (pd!=null)
                prodExist.getProductDetails().setId(pd.getId());
            product = prodExist;
        }
        if (file!=null) {
            String fileName = FilenameUtils.getName(file.getOriginalFilename());
            if (fileName != null && !fileName.isEmpty())
                product.getProductDetails().setPhotoLoc(SUB_FOLDER + fileName);
        }

        return product;
    }

    public void clearCache(){
        for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }

    }


    private void copyFile(MultipartFile file) throws IOException {
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        FileCopyUtils.copy(file.getBytes(), new File(SUB_FOLDER + fileName));
    }


}
