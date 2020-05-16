package com.webshop.entity;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Locale;

@Entity
@Table(name="product", uniqueConstraints={
        @UniqueConstraint(columnNames = {"details_id", "lang"})
})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Valid
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "details_id", nullable = false, referencedColumnName = "id")
    private ProductDetails productDetails;

    @Column(name="lang")
    private Locale lang;

    @NotNull
    @Size(min=2,max=20,message="Invalid length for Product Name")
    @Pattern(regexp="[A-Za-z0-9\\-(\\s)]+", message="Invalid Product Name")
    @Column(name="name")
    private String name;

    @Size(max=1000,message="Invalid length for Product description")
    @Pattern(regexp="[(\\w)(\\s)(\\-)(\\+)(\\*)(\\.),;:%!?@'\"]+", message="Invalid Product description")
    @Column(name="description")
    private String description;


    public Product() {}

    public Product(Category category, Locale lang,
                   String name, String description, ProductDetails productDetails) {
        this.setCategory(category);
        this.setLang(lang);
        setName(name);
        setDescription(description);
        setProductDetails(productDetails);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }




    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale locale) {
        this.lang = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
