package com.webshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class OrderCost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name="description")
    private String description;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="VAT")
    private BigDecimal vat;

    public BigDecimal getTotalPrice(){
        return price.multiply(new BigDecimal(quantity));
    }
    public BigDecimal getTotalVat(){
        return getTotalPrice().multiply(vat).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN);
    }
    public BigDecimal getTotalPriceInclVat(){
        return price.multiply(new BigDecimal(quantity)).add(getTotalVat());
    }

}
