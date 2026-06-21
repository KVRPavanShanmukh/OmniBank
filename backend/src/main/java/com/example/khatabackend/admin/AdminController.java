package com.example.khatabackend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://16.170.215.115:5173", allowCredentials = "true")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        return adminService.findByPhone(admin.getPhone())
            .filter(a -> a.getPassword().equals(admin.getPassword()))
            .map(a -> ResponseEntity.ok().body("Login successful"))
            .orElseGet(() -> ResponseEntity.status(401).body("Invalid phone or password"));
    }
}
