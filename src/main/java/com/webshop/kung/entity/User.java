package com.webshop.kung.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CUSTOMER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long id;

    @NotNull
    @Column(name="username")
    private String username;

    @NotNull
    @Column(name="family_name")
    private String familyName;

    @NotNull
    @Column(name="first_name")
    private String firstName;

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

    @NotNull
    @Column(name="password")
    private String password;

    public User(){
    }

    public User(String username, String familyName, String firstName,String email, String password) {
        setUsername(username);
        setFamilyName(familyName);
        setFirstName(firstName);
        setEmail(email);
        setPassword(password);
    }

    public User(String username, String familyName, String firstName, String address, String postNr, String city, Country country, String email, String tel) {
        setUsername(username);
        setFamilyName(familyName);
        setFirstName(firstName);
        setAddress(address);
        setPostNr(postNr);
        setCity(city);
        setCountry(country);
        setEmail(email);
        setTel(tel);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
