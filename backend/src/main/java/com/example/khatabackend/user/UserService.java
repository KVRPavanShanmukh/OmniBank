package com.example.khatabackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getDob() == null) throw new IllegalArgumentException("Date of birth is required");
        LocalDate dob = LocalDate.parse(user.getDob());
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18) throw new IllegalArgumentException("User must be 18 or older");
        user.setAge(age);
        return userRepository.save(user);
    }

    public Optional<User> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user by mobile (primary key)
    public User updateUser(String mobile, User updatedUser) {
        User existingUser = userRepository.findById(mobile)
                .orElseThrow(() -> new IllegalArgumentException("User with mobile " + mobile + " not found"));
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setDob(updatedUser.getDob());
        existingUser.setPassword(updatedUser.getPassword());

        LocalDate dob = LocalDate.parse(updatedUser.getDob());
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18) throw new IllegalArgumentException("User must be 18 or older");
        existingUser.setAge(age);

        return userRepository.save(existingUser);
    }

    // Delete user by mobile (primary key)
    public void deleteUserByMobile(String mobile) {
        if (!userRepository.existsById(mobile)) {
            throw new IllegalArgumentException("User with mobile " + mobile + " not found");
        }
        userRepository.deleteById(mobile);
    }
}
