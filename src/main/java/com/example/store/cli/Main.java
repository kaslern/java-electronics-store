package com.example.store.cli;

import com.example.store.model.*;
import com.example.store.repo.*;
import com.example.store.service.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        File ordersFile = new File("orders.dat");
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new FileOrderRepository(ordersFile);
        ProductManager productManager = new ProductManager(productRepository);
        CartManager cartManager = new CartManager(productManager);
        DiscountService discountService = new DiscountService();
        OrderService orderService = new OrderService(cartManager, orderRepository, discountService);
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

        List<Order> loadedOrders = orderRepository.findAll();
        System.out.println("==================================================");
        if (loadedOrders.isEmpty()) {
            System.out.println("No previously saved orders found in the file.");
        } else {
            System.out.println("Loaded orders from file (" + loadedOrders.size() + "):");
            for (Order order : loadedOrders) {
                System.out.println("   - ID: " + order.getOrderId() + " | Date: " + order.getOrderDate() + " | Total: " + order.getTotalPrice() + " PLN");
            }
        }
        System.out.println("==================================================\n");

        Product tv = new Electronics(
                UUID.randomUUID(),
                "Sony",
                "Bravia 55",
                BigDecimal.valueOf(1000),
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

        CommandManager commandManager = new CommandManager(cartManager, invoiceGenerator,
                orderService, productManager, discountService);
        commandManager.commandCenter();
    }
}
