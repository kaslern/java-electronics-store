package com.example.store.cli;

import com.example.store.model.*;
import com.example.store.repo.*;
import com.example.store.service.*;
import com.example.store.service.discount.DiscountService;
import com.example.store.validation.ApprovalChain;
import com.example.store.validation.RequiredFieldsStep;
import com.example.store.validation.StockAvailabilityStep;

import java.io.File;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Clock clock = Clock.system(ZoneId.of("Europe/Warsaw"));

        File ordersFile = new File("orders.dat");
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new FileOrderRepository(ordersFile);
        ProductManager productManager = new ProductManager(productRepository);
        ApprovalChain approvalChain = new ApprovalChain(List.of(
                new RequiredFieldsStep(),
                new StockAvailabilityStep()
        ));
        CartManager cartManager = new CartManager(productManager);
        DiscountService discountService = new DiscountService();
        OrderService orderService = new OrderService(cartManager, orderRepository, discountService, approvalChain, clock);
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator(clock);

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

        Product tv = Electronics.builder()
                .id(UUID.randomUUID())
                .manufacturer("Sony")
                .model("Bravia 55")
                .price(BigDecimal.valueOf(1000))
                .quantity(1)
                .build();

        Product laptop = Computer.builder()
                .id(UUID.randomUUID())
                .manufacturer("Dell")
                .model("XPS 15")
                .price(BigDecimal.valueOf(6500))
                .quantity(3)
                .processorType(ProcessorType.INTEL_I7)
                .diskSize(DiskSize.DISK_1_TB)
                .ramSize(RamSize.RAM_16GB)
                .build();

        Product smartphone = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("iPhone 15")
                .price(BigDecimal.valueOf(5000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();

        productManager.addProduct(tv);
        productManager.addProduct(laptop);
        productManager.addProduct(smartphone);

        CommandManager commandManager = new CommandManager(cartManager, invoiceGenerator,
                orderService, productManager, discountService);
        commandManager.commandCenter();
    }
}