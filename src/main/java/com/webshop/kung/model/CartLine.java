package com.webshop.kung.model;

import com.webshop.kung.entity.Product;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;

public class CartLine {

    @Valid
    private Product product;

    @NotNull(message="Can not be empty")
    @Min(message = "minimum 1 order is required", value = 1)
    @Digits(integer = 5, fraction = 0, message = "only numbers allowed (no decimals)")
    private Integer quantity;

    public CartLine(){}

    public CartLine(Product product, Integer quantity){
        setProduct(product);
        setQuantity(quantity);
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal result = new BigDecimal(0);
        try {
            result = product.getProductDetails().getPrice().multiply(new BigDecimal(quantity));
        } catch (Exception ex){
        }

        return result;
    }
}
