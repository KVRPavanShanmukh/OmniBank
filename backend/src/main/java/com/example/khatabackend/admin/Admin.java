package com.example.khatabackend.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")   // Table name in your MySQL DB
@Getter
@Setter
public class Admin {
    @Id
    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    // Add other admin-related fields if needed (e.g., name)
}
