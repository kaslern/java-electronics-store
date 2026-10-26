package com.example.store.cli;

import com.example.store.model.*;
import com.example.store.repo.InMemoryOrderRepository;
import com.example.store.repo.InMemoryProductRepository;
import com.example.store.repo.OrderRepository;
import com.example.store.repo.ProductRepository;
import com.example.store.service.CartManager;
import com.example.store.service.InvoiceGenerator;
import com.example.store.service.OrderService;
import com.example.store.service.ProductManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();
        ProductManager productManager = new ProductManager(productRepository);
        CartManager cartManager = new CartManager(productManager);
        OrderService orderService = new OrderService(cartManager, orderRepository);
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

        Product tv = new Electronics(
                UUID.randomUUID(),
                "Sony",
                "Bravia 55",
                BigDecimal.valueOf(3200),
                1
        );

        Product laptop = new Computer(
                UUID.randomUUID(),
                "Dell",
                "XPS 15",
                BigDecimal.valueOf(6500),
                3,
                ProcessorType.INTEL_I7,
                DiskSize.DISK_1_TB,
                RamSize.RAM_16GB
        );

        Product phone = new Smartphone(
                UUID.randomUUID(),
                "Apple",
                "iPhone 15",
                BigDecimal.valueOf(4800),
                10,
                Color.BLACK,
                BatteryCapacity.BATTERY_4500MAH,
                List.of("Charger", "Case")
        );

        productManager.addProduct(tv);
        productManager.addProduct(laptop);
        productManager.addProduct(phone);

        CommandManager commandManager = new CommandManager(cartManager, invoiceGenerator, orderService, productManager);
        commandManager.commandCenter();
    }
}
