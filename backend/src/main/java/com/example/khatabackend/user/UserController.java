package com.example.khatabackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{mobile}")
    public User getUserByMobile(@PathVariable String mobile) { // Change to mobile
        return userService.findByMobile(mobile)
            .orElseThrow(() -> new RuntimeException("User not found with mobile: " + mobile));
    }
}