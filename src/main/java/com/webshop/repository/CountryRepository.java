package com.webshop.repository;

import com.webshop.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Locale;

public interface CountryRepository extends CrudRepository<Country, Long> {
    List<Country> findByLang(Locale lang);
    List<Country> findByCode(String code);
    Country findByCodeAndLang(String code, Locale lang);
}
