package com.example.store.validation;

import com.example.store.model.Cart;
import com.example.store.model.Customer;

public record OrderContext(Customer customer, Cart cart) {}