package com.webshop.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Table(name = "product_details")
public class ProductDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    
    @NotNull
    @Size(min=2,max=5,message="Invalid length for Product Code")
    @Pattern(regexp="[(\\w)]+", message="Invalid Product Code")
    @Column(name="Code", unique = true, nullable = false)
    private String code;

    @NotNull(message="Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message="BTW is required")
    @Digits(integer=2, fraction=2)
    @Column(name = "BTW", nullable = false)
    private BigDecimal btw;


    @NotNull(message="unit for price calculation is required")
    @Max(value = 2)
    @Min(value = 0)
    @Column(name = "unit", nullable = false)
    private Integer unit;

    @Column(name="photo")
    private String photoLoc;

    @NotNull(message="Stock is required")
    @Min(0)
    @Column(name="stock")
    private Integer stock;

    @NotNull(message="transport points are required")
    @Digits(integer=2, fraction=2)
    @Column(name="transportPoints", nullable = false)
    private BigDecimal transportPoints;

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public ProductDetails() {}

    public ProductDetails(String code, BigDecimal price, BigDecimal btw, Integer unit, String photoLoc, Integer stock, BigDecimal transportPoints) {
        setCode(code);
        setPrice(price);
        setBtw(btw);
        setUnit(unit);
        setPhotoLoc(photoLoc);
        setStock(stock);
        setTransportPoints(transportPoints);
    }


    public BigDecimal getBtw() {
        return btw;
    }

    public void setBtw(BigDecimal btw) {
        this.btw = btw;
    }

    public BigDecimal getPriceInclBtw() {
        return getPrice().multiply(getBtw().add(new BigDecimal(100))).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getTransportPoints() {
        return transportPoints;
    }

    public void setTransportPoints(BigDecimal transportPoints) {
        this.transportPoints = transportPoints;
    }
}
