package com.webshop.repository;

import com.webshop.entity.Customer;
import com.webshop.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByCustomerAndStatus(Customer customer, Integer status);
    List<Order> findByCustomerAndStatusNot(Customer customer, Integer status);
    List<Order> findByStatus(Integer status);
    List<Order> findByReceiverName(String receiverName);
    List<Order> findByReceiverEmail(String receiverEmail);

    @Query("SELECT O FROM Order O WHERE LOWER(O.receiverName) like %:search% OR LOWER(O.receiverEmail) like %:search% OR LOWER(O.receiverCity) like %:search%")
    List<Order> searchByCustomerData(String search);

    @Query("SELECT O FROM Order O WHERE O.status=:status AND (LOWER(O.receiverName) like %:search% OR LOWER(O.receiverEmail) like %:search% OR LOWER(O.receiverCity) like %:search%)")
    List<Order> searchByStatusAndCustomerData(Integer status, String search);

    Order findByIdAndCustomerAndStatusNot(long id, Customer customer, Integer status);
}
