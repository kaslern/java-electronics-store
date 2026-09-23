package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComputerTest {

    private static final UUID DEFAULT_ID = UUID.randomUUID();
    private static final String DEFAULT_MANUFACTURER = "Dell";
    private static final String DEFAULT_MODEL = "XPS";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(4500);
    private static final int DEFAULT_QUANTITY = 1;
    private static final ProcessorType DEFAULT_PROCESSOR = ProcessorType.RYZEN_7;
    private static final DiskSize DEFAULT_DISK = DiskSize.DISK_1_TB;
    private static final RamSize DEFAULT_RAM = RamSize.RAM_16GB;

    private Computer createComputer(ProcessorType processorType, DiskSize diskSize, RamSize ramSize) {
        return new Computer(
                DEFAULT_ID,
                DEFAULT_MANUFACTURER,
                DEFAULT_MODEL,
                DEFAULT_PRICE,
                DEFAULT_QUANTITY,
                processorType,
                diskSize,
                ramSize
        );
    }

    @Test
    void shouldCreateObjectWithCorrectData() {
        // Act
        Computer computer = createComputer(DEFAULT_PROCESSOR, DEFAULT_DISK, DEFAULT_RAM);

        // Assert
        assertEquals(DEFAULT_ID, computer.getId());
        assertEquals(DEFAULT_MANUFACTURER, computer.getManufacturer());
        assertEquals(DEFAULT_MODEL, computer.getModel());
        assertEquals("Dell XPS", computer.getName());
        assertEquals(DEFAULT_PRICE, computer.getPrice());
        assertEquals(DEFAULT_QUANTITY, computer.getQuantity());
        assertEquals(DEFAULT_PROCESSOR, computer.getProcessorType());
        assertEquals(DEFAULT_DISK, computer.getDiskSize());
        assertEquals(DEFAULT_RAM, computer.getRamSize());
    }

    @Test
    void shouldThrowExceptionWhenProcessorIsNull() {
        assertThatThrownBy(() -> createComputer(null, DEFAULT_DISK, DEFAULT_RAM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Processor type cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDiskIsNull() {
        assertThatThrownBy(() -> createComputer(DEFAULT_PROCESSOR, null, DEFAULT_RAM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Disk size cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenRamIsNull() {
        assertThatThrownBy(() -> createComputer(DEFAULT_PROCESSOR, DEFAULT_DISK, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ram size cannot be null");
    }
}