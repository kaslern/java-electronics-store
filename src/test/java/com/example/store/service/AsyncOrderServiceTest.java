package com.example.store.service;

import com.example.store.model.*;
import com.example.store.repo.InMemoryOrderRepository;
import com.example.store.repo.InMemoryProductRepository;
import com.example.store.repo.OrderRepository;
import com.example.store.repo.ProductRepository;
import com.example.store.service.discount.DiscountService;
import com.example.store.validation.ApprovalChain;
import com.example.store.validation.RequiredFieldsStep;
import com.example.store.validation.StockAvailabilityStep;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class AsyncOrderServiceTest {

    private AsyncOrderService asyncOrderService;
    private SyncOrderService syncOrderService;
    private ExecutorService executor;
    private Customer customer;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();
        ProductManager productManager = new ProductManager(productRepository);
        CartManager cartManager = new CartManager(productManager);
        DiscountService discountService = new DiscountService();
        ApprovalChain approvalChain = new ApprovalChain(List.of(
                new RequiredFieldsStep(),
                new StockAvailabilityStep()
        ));
        Instant fixedInstant = Instant.parse("2026-10-26T10:00:00Z");
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        Clock fixedClock = Clock.fixed(fixedInstant, zoneId);

        OrderService orderService = new OrderService(cartManager, orderRepository, discountService, approvalChain, fixedClock);
        syncOrderService = new SyncOrderService(orderService);
        executor = Executors.newFixedThreadPool(4);
        asyncOrderService = new AsyncOrderService(orderService, executor);
        Address address = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");
        customer = new Customer(UUID.randomUUID(), "Janusz", "Nosacz", "kiedysToBylo@prl.ru", address);

    }

    @AfterEach
    void cleanUp() {
        executor.shutdownNow();
    }

    @Test
    void shouldProcessOrdersAsync() {
        //Arrange
        List<Cart> cartsSync = createCarts();
        List<Cart> cartsASync = createCarts();

        //Act
        long startSync = System.currentTimeMillis();

        List<Order> syncOrders = cartsSync.stream()
                .map(cart -> syncOrderService.processOrderSync(customer, cart))
                .toList();

        long timeSync = System.currentTimeMillis() - startSync;

        long startAsync = System.currentTimeMillis();
        List<CompletableFuture<Order>> futures = cartsASync.stream()
                .map(cart -> asyncOrderService.processOrderAsync(customer, cart))
                .toList();

        List<Order> asyncOrders = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        long timeAsync = System.currentTimeMillis() - startAsync;

        //Assert
        assertTrue(timeAsync < timeSync);
        System.out.println("Time sync: " + timeSync);
        System.out.println("Time async: " + timeAsync);
    }

    private List<Cart> createCarts() {
        List<Cart> carts = IntStream.range(0, 100)
                .mapToObj(i -> {
                    Cart cart = new Cart();
                    cart.addProduct(Smartphone.builder()
                            .id(UUID.randomUUID())
                            .manufacturer("Apple")
                            .model("Iphone 18")
                            .price(BigDecimal.valueOf(5000))
                            .quantity(1)
                            .color(Color.BLACK)
                            .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                            .accessories(List.of("Charger"))
                            .build());
                    return cart;
                })
                .toList();
        return carts;
    }

}