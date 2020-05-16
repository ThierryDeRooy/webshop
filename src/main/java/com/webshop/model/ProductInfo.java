package com.webshop.model;

import com.webshop.entity.Product;
import com.webshop.validator.PhotoFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public class ProductInfo {

    @Valid
    private Product product;

    @Valid
    @PhotoFile
    private MultipartFile file;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
