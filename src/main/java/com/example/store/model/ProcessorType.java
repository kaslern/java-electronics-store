package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum ProcessorType {
    INTEL_I7("Intel i7"),
    INTEL_I5("Intel i5"),
    RYZEN_7("Ryzen 7"),
    RYZEN_5("Ryzen 5");

    private final String displayName;
}
