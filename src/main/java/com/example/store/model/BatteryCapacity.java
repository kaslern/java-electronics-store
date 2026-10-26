package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum BatteryCapacity {
    BATTERY_3800MAH("Battery 3800mAh"),
    BATTERY_4500MAH("Battery 4500mAh"),
    BATTERY_5500MAH("Battery 5500mAh");

    private final String displayName;
}
