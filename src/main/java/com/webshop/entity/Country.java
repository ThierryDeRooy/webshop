package com.webshop.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

@Entity
@Table(name="country", uniqueConstraints={
        @UniqueConstraint(columnNames = {"code", "lang"})
})
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="country_id")
    private Long id;

    @NotNull
    @Size(min=1,max=16,message="Invalid length for Country name")
    @Column(name="country")
    private String country;

    @NotNull
    @Column(name="lang")
    private Locale lang;

    @NotNull
    @Size(min=1,max=5,message="Invalid length for Country code")
    @Column(name="code")
    private String code;

    public Country(){}


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang = lang;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
