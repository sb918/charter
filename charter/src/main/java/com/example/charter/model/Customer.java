package com.example.charter.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "customers")
@Table(name = "customers")
public class Customer {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "firstName")
    @NotBlank(message = "Customer's first name must not be empty")
    private String firstName;
    @Column(name = "lastName")
    @NotBlank(message = "Customer's last name must not be empty")
    private String lastName;
    @Column(name = "phoneNumber")
    @NotBlank(message = "Customer's phone number must not be empty")
    private String phoneNumber;
    @Column(name = "email")
    @NotBlank(message = "Customer's e-mail must not be empty")
    private String email;
    @Column(name = "licenceNumber")
    @NotBlank(message = "Customer's licence must not be empty")
    private String licenceNumber;



    public Customer(){

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }
}
