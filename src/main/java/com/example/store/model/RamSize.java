package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum RamSize {
    RAM_8GB("8GB"),
    RAM_16GB("16GB"),
    RAM_32GB("32GB");

    private final String displayName;
}
