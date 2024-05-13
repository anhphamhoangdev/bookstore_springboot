package com.example.bookstore.web;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RegisterUser {


    private String firstName;

    private String lastName;

    @Size(min = 2, max = 20, message = "Length of username must be between 2 and 20")
    private String username;

    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must have a minimum length of 8, at least 1 digit, and 1 special character.")
    private String password;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String gender;

    private String address;

    private String phoneNum;

    public RegisterUser() {
    }

    public RegisterUser(String address, Date birthday, String email, String firstName, String gender, String lastName, String password, String phoneNum, String username) {
        this.address = address;
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.gender = gender;
        this.lastName = lastName;
        this.password = password;
        this.phoneNum = phoneNum;
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @Size(min = 8, message = "Minimum length of password is 8") @Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must have a minimum length of 8, at least 1 digit, and 1 special character.") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 8, message = "Minimum length of password is 8") @Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must have a minimum length of 8, at least 1 digit, and 1 special character.") String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public @Size(min = 2, max = 20, message = "Length of username must be between 2 and 20") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 2, max = 20, message = "Length of username must be between 2 and 20") String username) {
        this.username = username;
    }
}
