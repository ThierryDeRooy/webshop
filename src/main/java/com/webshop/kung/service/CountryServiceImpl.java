package com.webshop.kung.service;

import com.webshop.kung.entity.Country;
import com.webshop.kung.entity.Product;
import com.webshop.kung.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAllCountries() {
        return (List<Country>) countryRepository.findAll();
    }

    @Override
    public List<Country> findAllCountries(Locale locale) {
        return countryRepository.findByLang(locale);
    }
}
