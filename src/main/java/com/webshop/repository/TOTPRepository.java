package com.webshop.repository;

import com.webshop.entity.TOTPDetails;
import org.springframework.data.repository.CrudRepository;

public interface TOTPRepository extends CrudRepository<TOTPDetails, String> {

    TOTPDetails findByUsername(String username);
    boolean existsByUsername(String username);
    Long deleteByUsername(String username);

}
