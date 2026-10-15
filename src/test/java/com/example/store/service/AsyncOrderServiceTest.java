package com.example.store.service;

import com.example.store.model.*;
import com.example.store.repo.InMemoryOrderRepository;
import com.example.store.repo.InMemoryProductRepository;
import com.example.store.repo.OrderRepository;
import com.example.store.repo.ProductRepository;
import com.example.store.service.discount.DiscountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


class AsyncOrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private CartManager cartManager;
    private ProductManager productManager;
    private ProductRepository productRepository;
    private DiscountService discountService;
    private AsyncOrderService asyncOrderService;
    private SyncOrderService syncOrderService;
    private ExecutorService executor;
    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        orderRepository = new InMemoryOrderRepository();
        productManager = new ProductManager(productRepository);
        cartManager = new CartManager(productManager);
        discountService = new DiscountService();
        orderService = new OrderService(cartManager, orderRepository, discountService);
        syncOrderService = new SyncOrderService(orderService);
        executor = Executors.newFixedThreadPool(4);
        asyncOrderService = new AsyncOrderService(orderService, executor);
        address = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");
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