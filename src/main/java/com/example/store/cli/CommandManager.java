package com.example.store.cli;

import com.example.store.exception.InsufficientStockException;
import com.example.store.model.*;
import com.example.store.service.*;
import com.example.store.service.discount.DiscountService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CommandManager {
    private final CartManager cartManager;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderService orderService;
    private final ProductManager productManager;
    private final Cart currentCart = new Cart();
    private final DiscountService discountService;

    public CommandManager(CartManager cartManager, InvoiceGenerator invoiceGenerator, OrderService orderService,
                          ProductManager productManager, DiscountService discountService) {
        this.cartManager = cartManager;
        this.invoiceGenerator = invoiceGenerator;
        this.orderService = orderService;
        this.productManager = productManager;
        this.discountService = discountService;
    }

    Scanner scanner = new Scanner(System.in);

    public void commandCenter() {
        boolean running = true;

        while (running) {
            printMenu();
            String userInput = scanner.nextLine();

            int choice = -1;
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Displaying products...");
                    handleShowProducts();
                }
                case 2 -> {
                    System.out.println("Adding to cart...");
                    handleAddToCart();
                }
                case 3 -> {
                    System.out.println("Cart contents...");
                    handleShowCart();
                }
                case 4 -> {
                    System.out.println("Placing order...");
                    handlePlaceOrder();
                }
                case 0 -> {
                    System.out.println("Thank you for visiting!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                =================================
                Welcome to Shitty Electronic Store! 🏪
                =================================
                1. 📦 Display products
                2. ➕ Add product to cart
                3. 🛒 View cart
                4. 💳 Place order
                0. 🚪 Exit
                =================================
                Select an option:""");
    }

    private void handleShowProducts() {
        List<Product> products = productManager.findAll();

        if (products.isEmpty()) {
            System.out.println("No products available in the store.");
            return;
        }
        for (Product product : products) {
            String extraDetails = "";

            if (product instanceof Computer computer) {
                extraDetails = """
                        💻 Processor: %s
                        🧠 RAM: %s
                        💾 Disk: %s""".formatted(
                        computer.getProcessorType().getDisplayName(),
                        computer.getRamSize().getDisplayName(),
                        computer.getDiskSize().getDisplayName()
                );
            } else if (product instanceof Smartphone phone) {
                extraDetails = """
                        🎨 Color: %s
                        🔋 Battery: %s
                        🔌 Accessories: %s""".formatted(
                        phone.getColor(),
                        phone.getBatteryCapacity().getDisplayName(),
                        phone.getAccessories()
                );
            }

            System.out.println("""
                    --------------------------------------------------
                    🆔 ID: %s
                    🏷️ Name: %s
                    💰 Price: %s PLN
                    📦 Stock: %d pcs
                    %s
                    --------------------------------------------------""".formatted(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    extraDetails
            ));
        }
    }

    private void handleAddToCart() {
        System.out.println("Enter product ID:");
        String userInput = scanner.nextLine();

        try {
            UUID productId = UUID.fromString(userInput);
            Product product = productManager.findById(productId);
            if (product == null) {
                System.out.println("Product not found");
                return;
            }

            if (product instanceof Computer computer) {
                product = configureComputer(computer);
            } else if (product instanceof Smartphone smartphone) {
                product = configureSmartphone(smartphone);
            }

            currentCart.addProduct(product);
            System.out.println("Product added to cart!");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
        }
    }

    private Computer configureComputer(Computer base) {
        RamSize selectedRam = promptForChoice("Select RAM size:", RamSize.values());
        DiskSize selectedDisk = promptForChoice("Select DISK size:", DiskSize.values());
        ProcessorType selectedProcessor = promptForChoice("Select processor:", ProcessorType.values());

        return Computer.builder()
                .id(base.getId())
                .manufacturer(base.getManufacturer())
                .model(base.getModel())
                .price(base.getPrice())
                .quantity(base.getQuantity())
                .processorType(selectedProcessor)
                .diskSize(selectedDisk)
                .ramSize(selectedRam)
                .build();
    }

    private Smartphone configureSmartphone(Smartphone base) {
        Color selectedColor = promptForChoice("Select color:", Color.values());
        BatteryCapacity selectedBattery = promptForChoice("Select battery size:", BatteryCapacity.values());

        return Smartphone.builder()
                .id(base.getId())
                .manufacturer(base.getManufacturer())
                .model(base.getModel())
                .price(base.getPrice())
                .quantity(base.getQuantity())
                .color(selectedColor)
                .batteryCapacity(selectedBattery)
                .accessories(base.getAccessories())
                .build();    }


    private <T> T promptForChoice(String message, T[] options) {
        System.out.println(message);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int choice = -1;

        while (choice < 1 || choice > options.length) {
            try {
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(scanner.nextLine());

                if (choice < 1 || choice > options.length) {
                    System.out.println("Invalid option, try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        return options[choice - 1];
    }

    private void handleShowCart() {
        List<Product> products = cartManager.getAllProductsFromCart(currentCart);

        if (products.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }
        for (Product product : products) {
            String extraDetails = "";

            if (product instanceof Computer computer) {
                extraDetails = """
                        💻 Processor: %s
                        🧠 RAM: %s
                        💾 Disk: %s""".formatted(
                        computer.getProcessorType().getDisplayName(),
                        computer.getRamSize().getDisplayName(),
                        computer.getDiskSize().getDisplayName()
                );
            } else if (product instanceof Smartphone phone) {
                extraDetails = """
                        🎨 Color: %s
                        🔋 Battery: %s
                        🔌 Accessories: %s""".formatted(
                        phone.getColor(),
                        phone.getBatteryCapacity().getDisplayName(),
                        phone.getAccessories()
                );
            }

            System.out.println("""
                    --------------------------------------------------
                    🆔 ID: %s
                    🏷️ Name: %s
                    💰 Price: %s PLN
                    %s
                    --------------------------------------------------""".formatted(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    extraDetails
            ));
        }
    }

    private void handlePlaceOrder() {
        if (currentCart.getProducts().isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        Address address = promptForAddress();
        Customer customer = promptForCustomer(address);
        promptForPromoCode();

        try {
            Order order = orderService.createOrder(customer, currentCart);
            System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
            Invoice invoice = invoiceGenerator.generateInvoice(order);
            System.out.println(invoice.toString());
        } catch (InsufficientStockException e) {
            System.out.println(e.getMessage());
        }
    }

    private Address promptForAddress() {
        while (true) {
            try {
                System.out.println("Enter street:");
                String street = scanner.nextLine();
                System.out.println("Enter city:");
                String city = scanner.nextLine();
                System.out.println("Enter post code:");
                String postCode = scanner.nextLine();

                return new Address(street, city, postCode);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Customer promptForCustomer(Address address) {
        while (true) {
            try {
                System.out.println("Enter your name:");
                String firstName = scanner.nextLine();
                System.out.println("Enter your last name:");
                String lastName = scanner.nextLine();
                System.out.println("Enter your email:");
                String email = scanner.nextLine();

                return new Customer(UUID.randomUUID(), firstName, lastName, email, address);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void promptForPromoCode() {
        boolean running = true;
        while (running) {
            System.out.println("""
        --------------------------------------------------
        Do you have promo code?
        1. YES
        2. NO
        --------------------------------------------------""");
            String userInput = scanner.nextLine();

            int choice = -1;
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter promo code");
                    String userCode = scanner.nextLine();
                    if (discountService.isValidPromoCode(userCode)) {
                        currentCart.applyPromoCode(userCode);
                        System.out.println("Promo code applied!");
                        running = false;
                    } else {
                        System.out.println("Invalid promo code! Try again.");
                    }
                }

                case 2 -> running = false;
            }
        }
    }
}