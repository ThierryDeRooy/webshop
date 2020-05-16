package com.webshop.repository;

import com.webshop.entity.Verification;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<Verification, String> {
    Verification findByUsername(String username);
    boolean existsByUsername(String username);

}
