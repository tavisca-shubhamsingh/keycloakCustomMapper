package com.keycloak.mapper.service.service;

import com.keycloak.mapper.service.model.User;
import com.keycloak.mapper.service.reposiotry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getById(int id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get() : null;
    }

    public User saveUser(User user) {
        return userRepository.save(user);

    }
}
