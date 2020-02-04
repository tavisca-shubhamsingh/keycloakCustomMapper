package com.keycloak.mapper.service.controller;

import com.keycloak.mapper.service.model.User;
import com.keycloak.mapper.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/{id}")
    public User getByUserId(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

}
