package com.example.store.repo;

import com.example.store.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FileOrderRepositoryTest {

    @TempDir
    Path tempDir;

    private File testFile;
    private FileOrderRepository repository;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test_orders.dat").toFile();
        repository = new FileOrderRepository(testFile);
    }

    @Test
    void shouldReturnEmptyListForNewFile() {
        List<Order> orders = repository.findAll();

        assertThat(orders).isEmpty();
    }

    @Test
    void shouldSaveAndFindOrderById() {
        Order order = createSampleOrder();

        repository.save(order);
        Optional<Order> foundOrder = repository.findById(order.getOrderId());

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getOrderId()).isEqualTo(order.getOrderId());
        assertThat(foundOrder.get().getTotalPrice()).isEqualByComparingTo(order.getTotalPrice());
    }

    @Test
    void shouldPersistDataAcrossInstances() {
        Order order = createSampleOrder();
        repository.save(order);

        FileOrderRepository newRepository = new FileOrderRepository(testFile);
        List<Order> loadedOrders = newRepository.findAll();

        assertThat(loadedOrders).hasSize(1);
        assertThat(loadedOrders.get(0).getOrderId()).isEqualTo(order.getOrderId());
    }

    private Order createSampleOrder() {
        Address address = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");
        Customer customer = new Customer(UUID.randomUUID(), "Janusz", "Nosacz", "kiedysToBylo@prl.ru", address);
        Product tv = new Electronics(UUID.randomUUID(), "Sony", "Bravia", BigDecimal.valueOf(3000), 1);
        OrderItem item = new OrderItem(tv, 1, tv.getPrice());

        return new Order(UUID.randomUUID(), customer, List.of(item), BigDecimal.valueOf(3000), LocalDateTime.now());
    }
}