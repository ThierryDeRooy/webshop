package com.webshop.repository;

import com.webshop.entity.WebUser;
import org.springframework.data.repository.CrudRepository;

public interface WebUserRepository extends CrudRepository<WebUser, Long> {
    WebUser findByUsername(String username);
    WebUser findByEmail(String email);
}
