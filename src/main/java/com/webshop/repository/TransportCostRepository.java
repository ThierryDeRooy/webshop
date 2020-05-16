package com.webshop.repository;

import com.webshop.entity.TransportCost;
import org.springframework.data.repository.CrudRepository;

public interface TransportCostRepository extends CrudRepository<TransportCost, Long> {
    TransportCost findByCountryCode(String countryCode);
}
