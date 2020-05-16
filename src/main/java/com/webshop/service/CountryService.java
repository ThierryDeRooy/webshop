package com.webshop.service;

import com.webshop.entity.Country;

import java.util.List;
import java.util.Locale;

public interface CountryService {

    List<Country> findAllCountries();
    List<Country> findAllCountries(Locale locale);
    Country findById(Long id);
    List<Country> findByCode(String code);
    Country saveCountry(Country country);
    void removeCountry(String code);

}
