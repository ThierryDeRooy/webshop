package com.webshop.service;

import com.webshop.entity.Country;
import com.webshop.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Override
    public Country findById(Long id) {
        Optional<Country> countryOpt =  countryRepository.findById(id);
        if (countryOpt.isPresent())
            return countryOpt.get();
        else
            return null;
    }

    @Override
    public List<Country> findByCode(String code) {
        return countryRepository.findByCode(code);
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void removeCountry(String code) {
        List<Country> countries = countryRepository.findByCode(code);
        for (Country country : countries) {
            countryRepository.deleteById(country.getId());
        }
    }
}
