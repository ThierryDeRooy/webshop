package com.webshop.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="productName")
    private String productName;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Column(name="btw", nullable = false)
    private BigDecimal btw;

    @Column(name="unit")
    private Integer unit;

    @Column(name="discount")
    private BigDecimal discount;

    @Column(name="quantity")
    private Integer quantity;

    public OrderDetails(){
    }

    public OrderDetails(Order order, Product product, String productName, BigDecimal price, BigDecimal btw, Integer unit, BigDecimal discount, Integer quantity) {
        setOrder(order);
        setProduct(product);
        setProductName(productName);
        setPrice(price);
        setBtw(btw);
        setUnit(unit);
        setDiscount(discount);
        setQuantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalPrice() {
        return getPrice().multiply(new BigDecimal(getQuantity()));
    }

    public BigDecimal getPriceInclBtw() {
        return getPrice().multiply(getBtw().add(new BigDecimal(100))).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getTotalPriceInclBtw() {
        return getPriceInclBtw().multiply(new BigDecimal(getQuantity())).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getBtw() {
        return btw;
    }

    public void setBtw(BigDecimal btw) {
        this.btw = btw;
    }
}
