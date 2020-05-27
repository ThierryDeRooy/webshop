package com.webshop.service;

import com.webshop.entity.Customer;
import com.webshop.entity.Order;
import com.webshop.entity.WebUser;
import com.webshop.repository.CustomerRepository;
import com.webshop.repository.OrderRepository;
import com.webshop.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final WebUserRepository webUserRepository;
    private final OrderRepository orderRepository;

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

    @Override
    public void deleteCustomer(Customer customer) {
        if (customer == null)
            return;
        List<Order> ordersToRemoveCust = orderRepository.findByCustomer(customer);
        if (ordersToRemoveCust != null) {
            for (Order order : ordersToRemoveCust) {
                order.setCustomer(null);
                orderRepository.save(order);
            }
        }
        customerRepository.delete(customer);
    }

}
