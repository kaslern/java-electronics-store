package com.example.store.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@ToString
@EqualsAndHashCode
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final UUID customerId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Address address;

    public Customer(UUID customerId, String firstName, String lastName, String email, Address address) {
        validate(customerId, firstName, lastName, email, address);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    private void validate(UUID customerId, String firstName, String lastName, String email, Address address) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        if (name.trim().length() < 2 || name.trim().length() > 50) {
            throw new IllegalArgumentException(fieldName + " must be between 2 and 50 characters");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}