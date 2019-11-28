package com.webshop.kung.entity;

import javax.persistence.*;
import java.util.Locale;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="country_id")
    private Long id;

    @Column(name="country")
    private String country;

    @Column(name="lang")
    private Locale lang;

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
}
