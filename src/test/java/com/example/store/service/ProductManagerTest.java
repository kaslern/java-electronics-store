package com.example.store.service;

import com.example.store.model.*;
import com.example.store.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductManagerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManager productManager;

    private Smartphone smartphone;

    @BeforeEach
    void setUp() {
        UUID id = UUID.randomUUID();
        List<String> accessories = List.of("Charger");
        smartphone = new Smartphone(id, "Apple", "Iphone 18", BigDecimal.valueOf(5000), 1,
                Color.BLACK, BatteryCapacity.BATTERY_4500MAH, accessories);
    }

    @Test
    void shouldAddProductWithCorrectData() {
        //Arragne
        when(productRepository.findById(smartphone.getId())).thenReturn(Optional.empty());

        // Act
        productManager.addProduct(smartphone);

        // Verify
        verify(productRepository).save(smartphone);
    }

    @Test
    void shouldRemoveProductWithCorrectData() {
        // Arrange
        when(productRepository.findById(smartphone.getId())).thenReturn(Optional.of(smartphone));

        // Act
        productManager.removeProduct(smartphone.getId());

        // Verify
        verify(productRepository).deleteById(smartphone.getId());
    }

    @Test
    void shouldUpdateProductWithCorrectData() {
        // Arrange
        when(productRepository.findById(smartphone.getId())).thenReturn(Optional.of(smartphone));

        // Act
        productManager.update(smartphone);

        // Verify
        verify(productRepository).save(smartphone);
    }

    @Test
    void shouldFindProductById() {
        // Arrange
        when(productRepository.findById(smartphone.getId())).thenReturn(Optional.of(smartphone));

        // Act
        Product result = productManager.findById(smartphone.getId());

        //Assert
        assertEquals(smartphone, result);
    }

    @Test
    void shouldReturnListOfAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(smartphone));

        // Act
        List<Product> result = productManager.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(smartphone, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsExist(){
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of());

        // Act
        List<Product> result = productManager.findAll();

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowExceptionForAddProductAsNull() {
            // Act & Assert
            assertThatThrownBy(() -> productManager.addProduct(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product cannot be null");
        }

        @Test
        void shouldThrowExceptionForExistingProduct() {
            // Arrange
            when(productRepository.findById(smartphone.getId())).thenReturn(Optional.of(smartphone));

            // Act & Assert
            assertThatThrownBy(() -> productManager.addProduct(smartphone))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product already exists");
        }

        @Test
        void shouldThrowExceptionForRemoveProductAsNull() {
            // Act & Assert
            assertThatThrownBy(() -> productManager.removeProduct(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Id cannot be null");
        }

        @Test
        void shouldThrowExceptionForRemovingProduct () {
            // Arrange
            when(productRepository.findById(smartphone.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> productManager.removeProduct(smartphone.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product not exist");
        }

        @Test
        void shouldThrowExceptionForUpdateProductAsNull() {
            // Act & Assert
            assertThatThrownBy(() -> productManager.update(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product cannot be null");
        }

        @Test
        void shouldThrowExceptionForUpdateProductWhenNotExist() {
            // Arrange
            when(productRepository.findById(smartphone.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> productManager.update(smartphone))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product not exist");
        }

        @Test
        void shouldThrowExceptionForFindByIdAsNull() {
            // Act & Assert
            assertThatThrownBy(() -> productManager.findById(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Id cannot be null");
        }

        @Test
        void shouldThrowExceptionForFindByIdWhenNotExist() {
            // Arrange
            when(productRepository.findById(smartphone.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> productManager.findById(smartphone.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product not exist");
        }
    }

}