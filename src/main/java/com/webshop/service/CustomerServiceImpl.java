package com.webshop.service;

import com.webshop.entity.Customer;
import com.webshop.entity.WebUser;
import com.webshop.repository.CustomerRepository;
import com.webshop.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final WebUserRepository webUserRepository;


    @Override
    public Customer saveUser(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername( String username) {
        WebUser webUser = webUserRepository.findByUsername(username);
        if (webUser != null)
            return customerRepository.findByWebUser(webUser);
        return null;
    }

 }
