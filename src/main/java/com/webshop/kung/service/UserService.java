package com.webshop.kung.service;

import com.webshop.kung.entity.User;
import com.webshop.kung.model.UserRegistration;

public interface UserService {

    public User saveUser(UserRegistration userRegistration);
    public User saveUser(User user);
    public User findByUsername(String username);

}
