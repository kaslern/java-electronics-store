package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerTest {

    private UUID validCustomerId;
    private String validFirstName;
    private String validLastName;
    private String validEmail;
    private Address validAddress;

    @BeforeEach
    void setUp() {
        validCustomerId = UUID.randomUUID();
        validFirstName = "Janusz";
        validLastName = "Nosacz";
        validEmail = "kiedysToBylo@prl.ru";
        validAddress = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        assertThatThrownBy(() ->
                new Customer(null, validFirstName, validLastName, validEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer ID cannot be null");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldThrowExceptionWhenFirstNameIsNullOrEmpty(String invalidFirstName) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, invalidFirstName, validLastName, validEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First name cannot be null or empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"J", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void shouldThrowExceptionWhenFirstNameHasInvalidLength(String invalidFirstName) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, invalidFirstName, validLastName, validEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First name must be between 2 and 50 characters");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldThrowExceptionWhenLastNameIsNullOrEmpty(String invalidLastName) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, validFirstName, invalidLastName, validEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Last name cannot be null or empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"K", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void shouldThrowExceptionWhenLastNameHasInvalidLength(String invalidLastName) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, validFirstName, invalidLastName, validEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Last name must be between 2 and 50 characters");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldThrowExceptionWhenEmailIsNullOrEmpty(String invalidEmail) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, validFirstName, validLastName, invalidEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email cannot be null or empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"jan.kowalski", "jan@", "@domain.com", "jan@com"})
    void shouldThrowExceptionWhenEmailHasInvalidFormat(String invalidEmail) {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, validFirstName, validLastName, invalidEmail, validAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format: " + invalidEmail);
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThatThrownBy(() ->
                new Customer(validCustomerId, validFirstName, validLastName, validEmail, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Address cannot be null");
    }
}