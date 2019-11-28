package com.webshop.kung.service;

import com.webshop.kung.entity.Country;

import java.util.List;
import java.util.Locale;

public interface CountryService {

    List<Country> findAllCountries();
    List<Country> findAllCountries(Locale locale);

}
