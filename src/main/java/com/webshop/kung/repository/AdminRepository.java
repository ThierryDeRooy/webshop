package com.webshop.kung.repository;

import com.webshop.kung.entity.Admin;
import com.webshop.kung.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    Admin findByUsername(String username);
}
