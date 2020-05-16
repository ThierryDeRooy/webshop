package com.webshop.service;

import com.webshop.entity.Category;
import com.webshop.entity.ProductDetails;
import com.webshop.exception.ProductException;
import com.webshop.constants.Constants;
import com.webshop.entity.Product;
import com.webshop.repository.ProductDetailsRepository;
import com.webshop.repository.ProductRepository;
import com.webshop.utils.FileTools;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${file.mainfolder}")
    private String UPLOADED_FOLDER;
    @Value("${file.subfolder}")
    private String SUB_FOLDER;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CacheManager cacheManager;   // autowire cache manager


//    @Override
//    public List<Product> findAllProducts() {
//        return (List<Product>)productRepository.findAll();
//    }

    @Override
    @Cacheable("Product")
    public List<Product> findAllProducts(Locale locale) {
        return (List<Product>)productRepository.findByLang(locale);
    }

    @Override
    public List<Product> findMissingProducts(Locale locale) {
        return productRepository.findMissingProducts(locale);
    }

//    @Override
//    @Cacheable("Product")
//    public List<Product> findAllProductsByCategory(Category category) {
//        List<Product> productList = productRepository.findByCategory(category);
//        return productList;
//    }

    private Page<Product> findAllProductsByCategoriesAndStock(List<Category> categoryList, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction) ,sortBy));
        List<String> codeList = new ArrayList<>();
        for (Category cat : categoryList)
            codeList.add(cat.getCode());
        Locale loc = new Locale("en");
        if (categoryList.size()>0)
            loc =categoryList.get(0).getLang();
        Page<Product> pagedResult = productRepository.findByCategoriesAndStock(codeList, loc, Constants.DEFAULT_LOCALE, stock, paging);
        return pagedResult;
    }


    private Page<Product> searchAllProductsByCategoriesAndStock(List<Category> categoryList, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction, String search) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction) ,sortBy));
        Page<Product> pagedResult = productRepository.searchByCategoriesAndStock(categoryList,stock,search.toLowerCase(),paging);
        return pagedResult;
    }

    @Override
    public Product addProduct(Product product) {
        productDetailsRepository.save(product.getProductDetails());
        return productRepository.save(product);
    }


    private Product addProductOnly(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Locale locale, Product product, MultipartFile file, int addToStock) throws IOException, ProductException {
        if (file!=null && !file.isEmpty()) {
            FileTools.copyFile(file, UPLOADED_FOLDER+SUB_FOLDER);
        }
        ProductDetails productDetails = null;
        if (product.getProductDetails().getId()!=null) {
            productDetails = findById(product.getProductDetails().getId());
            if (productDetails==null)
                throw new ProductException("ProductID doesn't exist");
        } else {
            productDetails = findByCode(product.getProductDetails().getCode());
        }
        if (productDetails!=null) {
            if (product.getProductDetails().getId()==null && productRepository.findByCodeAndLang(locale, product.getProductDetails().getCode())!=null)
                throw new ProductException("ProductCode already exists");
            if (product.getProductDetails().getId()!=null && product.getProductDetails().getId()!=productDetails.getId())
                throw new ProductException("ProductCode already exists");
            if (productDetails.getStock()+addToStock<0)
                throw new ProductException("Stock is negative");
        }

        if (productDetails!=null) {
            product.getProductDetails().setId(productDetails.getId());
            if (product.getProductDetails().getPhotoLoc() == null) {
                product.getProductDetails().setPhotoLoc(productDetails.getPhotoLoc());
            }
        }
        if (file!=null) {
            String fileName = FilenameUtils.getName(file.getOriginalFilename());
            if (fileName != null && !fileName.isEmpty())
                product.getProductDetails().setPhotoLoc(SUB_FOLDER + fileName);
        }
        saveDetails(product.getProductDetails(), addToStock);
        Product prodExist = findByLangAndCode(product.getLang(),product.getProductDetails().getCode());
        if (prodExist!=null) {
            product.setId(prodExist.getId());
        }

        addProductOnly(product);
        clearCache();


    }

    @Override
    public void updateStock(ProductDetails productDetails, int stock) {
        updateDetails(productDetails, stock, true);
        clearCache();
    }


    private void saveDetails(ProductDetails productDetails, int stock) {
        updateDetails(productDetails, stock, false);
    }

    private synchronized void updateDetails(ProductDetails productDetails, int stockToAdd, boolean onlyStock){
        ProductDetails prdDetails = productDetailsRepository.findByCode(productDetails.getCode());
        if (onlyStock) {
            prdDetails.setStock(prdDetails.getStock() + stockToAdd);
            productDetailsRepository.save(prdDetails);
        } else {
            int stock = 0;
            if (prdDetails != null)
                stock = prdDetails.getStock();
            productDetails.setStock(stock + stockToAdd);
            productDetailsRepository.save(productDetails);
        }
    }


    private ProductDetails findByCode(String code) {
        return productDetailsRepository.findByCode(code);
    }

    @Override
    public ProductDetails findById(Long id) {
        Optional<ProductDetails> pd = productDetailsRepository.findById(id);
        if (pd.isPresent())
            return pd.get();
        else
            return null;
    }

    @Override
    @Cacheable("Product")
    public Product findByCode(Locale loc, String code) {
        return productRepository.findByCode(loc, code);
    }


    private Product findByLangAndCode(Locale loc, String code) {
        Optional<Product> prod = productRepository.findByLangAndCode(loc, code);
        if (prod.isPresent())
            return prod.get();
        else
            return null;
    }

//    @Override
//    @Cacheable("Product")
//    public List<Product> findByLocaleName(Locale locale, String name) {
//        return productRepository.findByLangAndName(locale, name);
//    }

//    @Override
//    @Cacheable("Product")
//    public List<Product> findAllProductsByStock(Locale locale, int stock) {
//        return productRepository.findByLangAndStock(locale, stock);
//    }


    @Cacheable("Product")
    private Page<Product> findAllProductsByStock(Locale locale, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction) ,sortBy));
        Page<Product> pagedResult = productRepository.findByLangAndStock(locale, Constants.DEFAULT_LOCALE, stock, paging);
        return pagedResult;
    }

    private Page<Product> searchAllProductsByStock(Locale locale, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction, String search) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction) ,sortBy));
        Page<Product> pagedResult = productRepository.searchByLangAndStock(locale, Constants.DEFAULT_LOCALE, stock, search.toLowerCase(), paging);
        return pagedResult;
    }

    @Override
    public Page<Product> getAllProductsByStock(Locale locale, int stock, Integer pageNo, Integer pageSize, String sortBy, String direction, String search, Category cat) {
        Page<Product> pagedResult = null;
        if (cat != null && !"ALL".equalsIgnoreCase(cat.getName())) {
            List<Category> subCats = categoryService.getAllSubCategories(cat);
            if (search==null)
                pagedResult = findAllProductsByCategoriesAndStock(subCats, 0,
                        pageNo, pageSize, sortBy, direction);
            else
                pagedResult = searchAllProductsByCategoriesAndStock(subCats, 0,
                        pageNo, pageSize, sortBy, direction, search);
        } else {
            if (search==null)
                pagedResult = findAllProductsByStock(locale, 0,
                        pageNo, pageSize, sortBy, direction);
            else
                pagedResult = searchAllProductsByStock(locale, 0,
                        pageNo, pageSize, sortBy, direction, search);
        }
        return pagedResult;

    }


    public void clearCache(){
        for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }

    }

}

