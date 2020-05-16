package com.webshop.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity()
@Table(name="category", uniqueConstraints={
        @UniqueConstraint(columnNames = {"code", "lang"})
})
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="category_id")
    private Long id;

    @NotNull
    @Column(name="lang")
    private Locale lang;

    @NotNull
    @Size(min=1,max=15,message="Invalid length for Category name")
    @Column(name="name")
    private String name;

    @NotNull
    @Size(min=1,max=6,message="Invalid length for Category code")
    @Column(name="code")
    private String code;

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

    public Category(Category upperCategory, Locale lang, String name, String code) {
        this.setUpperCategory(upperCategory);
        this.setName(name);
        this.setLang(lang);
        this.setCode(code);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
