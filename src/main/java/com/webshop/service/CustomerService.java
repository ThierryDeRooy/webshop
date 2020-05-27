package com.webshop.service;

import com.webshop.entity.Customer;

public interface CustomerService {

    public Customer saveUser(Customer customer);
    public Customer findByUsername( String username);
    public void deleteCustomer(Customer customer);

}
