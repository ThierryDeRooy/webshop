package com.webshop.kung.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name="CATEGORY")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;

    @Column(name="lang")
    private Locale lang;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upper_category", nullable = true)
    private Category upperCategory;

    @OneToMany(mappedBy = "category",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;


    @Transient
    private List<Category> subCategories = new ArrayList<Category>();


    public Category(){
        super();
    }

    public Category(Category upperCategory, Locale lang, String name) {
        this.setUpperCategory(upperCategory);
        this.setName(name);
        this.setLang(lang);
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(Category subCategory) {
        this.subCategories.add(subCategory);
    }

    public Category getUpperCategory() {
        return upperCategory;
    }

    public void setUpperCategory(Category upperCategory) {
        this.upperCategory = upperCategory;
    }


    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getFullName(){
        String fullName = name;
        Category cat = this;
        while ((cat = cat.getUpperCategory()) != null) {
            fullName = cat.getName() + "/" +fullName;
        }
        return fullName;
    }
}
