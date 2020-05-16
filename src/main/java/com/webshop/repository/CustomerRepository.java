package com.webshop.repository;

import com.webshop.entity.Customer;
import com.webshop.entity.WebUser;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByWebUser(WebUser webUser);

}
