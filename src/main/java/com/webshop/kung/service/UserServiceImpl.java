package com.webshop.kung.service;

import com.webshop.kung.entity.User;
import com.webshop.kung.model.UserRegistration;
import com.webshop.kung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public User saveUser(UserRegistration userRegistration) {
        User user = new User(userRegistration.getUsername(),
                userRegistration.getLastname(),
                userRegistration.getFirstname(),
                userRegistration.getEmail(),
                encoder.encode(userRegistration.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
