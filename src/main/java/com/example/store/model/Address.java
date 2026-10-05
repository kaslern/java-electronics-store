package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@ToString
@EqualsAndHashCode

public class Address implements Serializable {
    private static final Pattern POSTCODE_PATTERN = Pattern.compile("^\\d{2}-\\d{3}$");
    private final String street;
    private final String city;
    private final String postCode;

    public Address(String street, String city, String postCode) {
        validateText(street, "Street");
        validateText(city, "City");
        validatePostCode(postCode);
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }

    private void validateText(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }

        if (text.trim().length() < 2 || text.trim().length() > 100) {
            throw new IllegalArgumentException(fieldName + " must be between 2 and 100 characters");
        }
    }

    private void validatePostCode(String postCode) {
        if (postCode == null || postCode.isBlank()) {
            throw new IllegalArgumentException("Post code cannot be null or empty");
        }

        if(!POSTCODE_PATTERN.matcher(postCode).matches()) {
            throw new IllegalArgumentException("Invalid post code format: " + postCode);
        }
    }
}
