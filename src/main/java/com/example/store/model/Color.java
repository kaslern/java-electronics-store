package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum Color {
    BLACK("Black"),
    WHITE("White"),
    SILVER("Silver");

    private final String displayName;
}
