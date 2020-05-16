package com.webshop.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Long id;

    @Column(name="date")
    private Date date;

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderCost> costs;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;
//    @Column(name="customer_id")
//    private Long customer;

    @Column(name="receiver_name")
    private String receiverName;

    @Column(name="receiver_address")
    private String receiverAddress;

    @Column(name="receiver_postal_nr")
    private String receiverPostNr;

    @Column(name="receiverCity")
    private String receiverCity;

    @ManyToOne
    @JoinColumn(name = "receiver_country_id", nullable = false)
    private Country receiverCountry;

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="receiver_tel")
    private String receiverTel;

    @Column(name="receiver_btw")
    private String receiverBtw;

    @Column(name="status")
    private Integer status;

    public Order() {}

    public Order(Date date, String receiverName, String receiverAddress, String receiverPostNr, String receiverCity,
                 Country receiverCountry, String receiverEmail, String receiverTel, String receiverBtw) {
        setDate(date);
        setReceiverName(receiverName);
        setReceiverAddress(receiverAddress);
        setReceiverPostNr(receiverPostNr);
        setReceiverCity(receiverCity);
        setReceiverCountry(receiverCountry);
        setReceiverEmail(receiverEmail);
        setReceiverTel(receiverTel);
        setReceiverBtw(receiverBtw);
    }

    public Order(Date date, Customer customer, String receiverName, String receiverAddress, String receiverPostNr, String receiverCity,
                 Country receiverCountry, String receiverEmail, String receiverTel, String receiverBtw) {
        this(date, receiverName, receiverAddress, receiverPostNr, receiverCity,
                receiverCountry, receiverEmail, receiverTel, receiverBtw);
        setCustomer(customer);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetailsList) {
        this.orderDetails = orderDetailsList;
    }

    public void addOrderDetail(OrderDetails orderDetail) {
        if (orderDetails==null) {
            orderDetails = new ArrayList<>();
        }
        orderDetails.add(orderDetail);
    }

    public List<OrderCost> getOrderCosts() { return costs;}
    public void addOrderCost(OrderCost cost){
        if (costs==null)
            costs = new ArrayList<>();;
            costs.add(cost);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPostNr() {
        return receiverPostNr;
    }

    public void setReceiverPostNr(String receiverPostNr) {
        this.receiverPostNr = receiverPostNr;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public Country getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(Country receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getQuantity() {
        int quantity = 0;
        for (OrderDetails orderDetail : orderDetails){
            quantity = quantity + orderDetail.getQuantity();
        }
        return quantity;
//        return orderDetails.size();
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderDetails orderDetails : getOrderDetails()) {
            totalPrice = totalPrice.add(orderDetails.getTotalPrice());
        }
        return totalPrice;
    }

    public BigDecimal getTotalPriceInclBtw() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderDetails orderDetails : getOrderDetails()) {
            totalPrice = totalPrice.add(orderDetails.getTotalPriceInclBtw());
        }
        return totalPrice;
    }
    public BigDecimal getTotalPriceInclTransport() {
        BigDecimal totalPrice = getTotalPrice();
        for (OrderCost orderCost : getOrderCosts()) {
            totalPrice = totalPrice.add(orderCost.getTotalPrice());
        }
        return totalPrice;
    }
    public BigDecimal getTotalPriceInclBtwTransport() {
        BigDecimal totalPrice = getTotalPriceInclBtw();
        for (OrderCost orderCost : getOrderCosts()) {
            totalPrice = totalPrice.add(orderCost.getTotalPriceInclVat());
        }
        return totalPrice;
    }

    public String getReceiverBtw() {
        return receiverBtw;
    }

    public void setReceiverBtw(String receiverBtw) {
        this.receiverBtw = receiverBtw;
    }
}
