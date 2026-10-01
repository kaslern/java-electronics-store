package com.example.store.service;


import com.example.store.model.Invoice;
import com.example.store.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class InvoiceGeneratorTest {

    private InvoiceGenerator invoiceGenerator;

    @Mock
    private Order order;

    @BeforeEach
    void setUp() {
        invoiceGenerator = new InvoiceGenerator();
    }

    @Test
    void shouldGenerateCorrectInvoiceNumbers() {
        // Arrange
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String expectedInvoiceNumber = "INV-%s-0001".formatted(expectedDate);
        String expectedInvoiceNumber2 = "INV-%s-0002".formatted(expectedDate);

        // Act
        Invoice invoice1 = invoiceGenerator.generateInvoice(order);
        Invoice invoice2 = invoiceGenerator.generateInvoice(order);

        // Assert
        assertThat(invoice1.invoiceNumber()).endsWith("-0001");
        assertThat(invoice2.invoiceNumber()).endsWith("-0002");
        assertThat(invoice1.invoiceNumber()).isEqualTo(expectedInvoiceNumber);
        assertThat(invoice2.invoiceNumber()).isEqualTo(expectedInvoiceNumber2);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        // Act & Assert
        assertThatThrownBy(() -> invoiceGenerator.generateInvoice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order must not be null");
    }
}