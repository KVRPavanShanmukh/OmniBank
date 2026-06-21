package com.example.khatabackend.auth;

import com.example.khatabackend.user.User;
import com.example.khatabackend.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://16.170.215.115:5173", allowCredentials = "true")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        logger.info("Received signup request for user: {}", user);
        try {
            User savedUser = userService.saveUser(user);
            logger.info("User saved successfully: {}", savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument in signup: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation in signup: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Email or mobile number already exists"));
        } catch (Exception e) {
            logger.error("Server error in signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        logger.info("Received login request for mobile: {}", user.getMobile());
        try {
            if (user == null || user.getMobile() == null || user.getPassword() == null)
                return ResponseEntity.badRequest().body(Map.of("message", "Mobile and password required"));

            Optional<User> optionalUser = userService.findByMobile(user.getMobile());
            if (optionalUser.isEmpty())
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));

            User existingUser = optionalUser.get();
            if (!user.getPassword().equals(existingUser.getPassword()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid mobile number or password"));
            Map<String, String> response = new HashMap<>();
            response.put("mobile", existingUser.getMobile());
            response.put("name", existingUser.getName());
            response.put("email", existingUser.getEmail());
            response.put("message", "Phone and password verified, please proceed with OTP");
            logger.info("Login successful for mobile: {}", user.getMobile());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during login for mobile: {}", user.getMobile(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        logger.info("Received request to fetch all users");
        return userService.getAllUsers();
    }

    // Update user by mobile (primary key)
    @PutMapping("/users/{mobile}")
    public ResponseEntity<?> updateUser(@PathVariable String mobile, @RequestBody User updatedUser) {
        logger.info("Received request to update user with mobile: {}", mobile);
        try {
            User savedUser = userService.updateUser(mobile, updatedUser);
            logger.info("User with mobile {} updated successfully", mobile);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument while updating user with mobile {}: {}", mobile, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Server error while updating user with mobile {}: {}", mobile, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update user", "error", e.getMessage()));
        }
    }

    // Delete user by mobile (primary key)
    @DeleteMapping("/users/{mobile}")
    public ResponseEntity<?> deleteUser(@PathVariable String mobile) {
        logger.info("Received request to delete user with mobile: {}", mobile);
        try {
            userService.deleteUserByMobile(mobile);
            logger.info("User with mobile {} deleted successfully", mobile);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument while deleting user with mobile {}: {}", mobile, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Server error while deleting user with mobile {}: {}", mobile, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to delete user", "error", e.getMessage()));
        }
    }

    @GetMapping("/users/{mobile}")
    public ResponseEntity<?> getUserByMobile(@PathVariable String mobile) {
        logger.info("Received request to fetch user with mobile: {}", mobile);
        try {
            Optional<User> optionalUser = userService.findByMobile(mobile);
            if (optionalUser.isEmpty()) {
                logger.warn("User not found with mobile: {}", mobile);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found with mobile: " + mobile));
            }
            User user = optionalUser.get();
            logger.info("Successfully fetched user with mobile: {}", mobile);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error fetching user with mobile {}: {}", mobile, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to fetch user", "error", e.getMessage()));
        }
    }
}
