package com.webshop.model;

import com.webshop.entity.Country;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerInfo {

    @NotNull(message = "name is required")
    @Size(max = 30, message="maximum length = 30")
    private String name;

    @NotNull(message = "please fill in your email")
    @Email
    private String email;

    @Pattern(regexp = "((\\+)?[\\- 0-9]+)", message = "required format= +xx xxxxxxxxx")
    private String phone;

    @Pattern(regexp = "([\\- 0-9a-zA-Z]+)", message = "VAT number not valid")
    @Size(max = 20, message="maximum length = 20")
    private String btwNr;

    @NotNull(message = "address is required")
    @Size(max = 60, message="maximum length = 60")
    private String address;

    @NotNull(message = "city is required")
    @Size(max = 20, message="maximum length = 20")
    private String city;

    @NotNull(message = "post code is required")
    @Size(max = 10, message="maximum length = 10")
    private String postCode;
    @Valid
    private Country country;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getBtwNr() {
        return btwNr;
    }

    public void setBtwNr(String btwNr) {
        this.btwNr = btwNr;
    }
}
