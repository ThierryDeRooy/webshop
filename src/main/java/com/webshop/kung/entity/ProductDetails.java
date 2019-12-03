package com.webshop.kung.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class ProductDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    
    @NotNull
    @Size(min=2,max=5,message="Invalid length for Product Code")
    @Pattern(regexp="[(\\w)]+", message="Invalid Product Code")
    @Column(name="Code", unique = true, nullable = false)
    private String code;

    @NotNull(message="Price is required")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message="unit for price calculation is required")
    @Max(value = 2)
    @Min(value = 0)
    @Column(name = "unit", nullable = false)
    private Integer unit;

    @Column(name="photo")
    private String photoLoc;

    @NotNull(message="Product status is required")
    @Max(value = 1)
    @Min(value = 0)
    @Column(name="status")
    private Integer status;

    @OneToMany(targetEntity=Product.class, mappedBy="productDetails")
    private List<Product> productList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getPhotoLoc() {
        return photoLoc;
    }

    public void setPhotoLoc(String photoLoc) {
        this.photoLoc = photoLoc;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public ProductDetails() {}

    public ProductDetails(String code, BigDecimal price, Integer unit, String photoLoc, Integer productStatus) {
        setCode(code);
        setPrice(price);
        setUnit(unit);
        setPhotoLoc(photoLoc);
        setStatus(productStatus);
    }


}
