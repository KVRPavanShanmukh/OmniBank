package com.example.khatabackend.user;

public class UserProfileDTO {
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String dob;
    private Integer age;

    // Constructor copies properties from User
    public UserProfileDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.gender = user.getGender();
        this.dob = user.getDob();
        this.age = user.getAge();
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
