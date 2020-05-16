package com.webshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name="transport_cost", uniqueConstraints={
        @UniqueConstraint(columnNames = {"country_id"})
})
public class TransportCost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @NotNull(message="transport points required")
    @Digits(integer=6, fraction=2)
    @Column(name="points")
    private BigDecimal points;

    @NotNull(message="cost price required")
    @Digits(integer=6, fraction=2)
    @Column(name="cost_price")
    private BigDecimal costPrice;

    @NotNull(message="VAT(%) required")
    @Digits(integer=2, fraction=2)
    @Column(name="VAT")
    private BigDecimal vat;

}
