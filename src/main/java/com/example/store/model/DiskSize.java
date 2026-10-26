package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor


public enum DiskSize {
    DISK_512_GB("512 GB"),
    DISK_1_TB("1 TB"),
    DISK_2_TB("2 TB");

    private final String description;
}