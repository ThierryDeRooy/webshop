package com.webshop.kung.repository;

import com.webshop.kung.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Locale;

public interface CountryRepository extends CrudRepository<Country, Long> {
    List<Country> findByLang(Locale lang);
}
