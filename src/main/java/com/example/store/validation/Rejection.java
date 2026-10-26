package com.example.store.validation;

public record Rejection(String step, String reason) {
    @Override public String toString() { return step + ": " + reason; }
}
