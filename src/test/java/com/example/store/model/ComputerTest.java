package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ComputerTest {

    @Test
    void shouldCreateObjectWithCorrectData() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Computer computer = new Computer(id, "Dell", BigDecimal.valueOf(4500), 1,
                ProcessorType.RYZEN_7, RamSize.RAM_16GB);

        // Assert
        assertEquals(id, computer.getId());
        assertEquals("Dell", computer.getName());
        assertEquals(BigDecimal.valueOf(4500), computer.getPrice());
        assertEquals(1, computer.getQuantity());
        assertEquals(ProcessorType.RYZEN_7, computer.getProcessorType());
        assertEquals(RamSize.RAM_16GB, computer.getRamSize());
    }

    @Test
    void shouldThrowExceptionWhenProcessorIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();

        //Act & Assert
        assertThatThrownBy(() -> new Computer(id, "Dell", BigDecimal.valueOf(4500), 1,
                null, RamSize.RAM_16GB))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Processor type cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenRamIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();

        //Act & Assert
        assertThatThrownBy(() -> new Computer(id, "Dell", BigDecimal.valueOf(4500), 1,
                ProcessorType.RYZEN_7, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ram size cannot be null");
    }

}