package com.webshop.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="customer_id")
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="webUser_id", referencedColumnName = "id")
    private WebUser webUser;

    @NotNull
    @Column(name="family_name")
    private String familyName;

    @NotNull
    @Column(name="first_name")
    private String firstName;

    @Column(name="company")
    private String company;

    @Column(name="address")
    private String address;

    @Column(name="postal_nr")
    private String postNr;

    @Column(name="city")
    private String city;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @NotNull
    @Email
    @Column(name="email")
    private String email;

    @Column(name="tel")
    private String tel;

    @Column(name="BTW")
    private String btwNr;

    public Customer(){
    }

    public Customer(WebUser webUser, String familyName, String firstName, String email) {
        setWebUser(webUser);
        setFamilyName(familyName);
        setFirstName(firstName);
        setEmail(email);
    }

    public Customer(WebUser webUser, String familyName, String firstName, String company, String address, String postNr, String city, Country country,
                    String email, String tel, String btwNr) {
        setWebUser(webUser);
        setFamilyName(familyName);
        setFirstName(firstName);
        setCompany(company);
        setAddress(address);
        setPostNr(postNr);
        setCity(city);
        setCountry(country);
        setEmail(email);
        setTel(tel);
        setBtwNr(btwNr);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostNr() {
        return postNr;
    }

    public void setPostNr(String postNr) {
        this.postNr = postNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public String getBtwNr() {
        return btwNr;
    }

    public void setBtwNr(String btwNr) {
        this.btwNr = btwNr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
