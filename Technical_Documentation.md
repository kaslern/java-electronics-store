# Sklep Elektroniczny – Dokumentacja Projektu

Głównym założeniem biznesowym projektu było stworzenie sklepu internetowego z artykułami elektronicznymi. Obsługa sklepu odbywa się poprzez interfejs CLI, który umożliwia przeglądanie zawartości sklepu, konfigurowanie własnych setupów sprzętowych, przeglądanie koszyka oraz implementację kodów rabatowych.

## 1. Opis biznesowy

Aplikacja symuluje podstawowy proces zakupowy – od przeglądania produktów po złożenie zamówienia i wyliczenie ostatecznej ceny.

### Co potrafi aplikacja?

* **Katalog produktów:** Przeglądanie dostępnego sprzętu (np. komputery, telefony) oraz sprawdzanie ich ilości w magazynie.

* **Koszyk zakupowy:** Dodawanie wybranego towaru do koszyka wraz z określeniem ilości.

* **Obsługa rabatów:**

  * Automatyczny rabat 10% na zakupy składane w piątki.

  * Możliwość wpisania kodu rabatowego (np. `SUMMER10` na 10% lub `VIP20` na 20%).

  * System sam wybiera korzystniejszy rabat dla klienta.

* **Przetwarzanie zamówień:** Obsługa zamówień w trybie synchronicznym oraz asynchronicznym (w tle).

### Zestawienie funkcji

| Funkcja | Opis działania | 
| ----- | ----- | 
| Dodawanie do koszyka | Wybór produktu i ilości z kontrolą stanu magazynowego | 
| Sprawdzanie kodów | Walidacja czy podany kod promocyjny istnieje | 
| Wyliczanie rabatu | Porównanie zniżki piątkowej i kodu, wybór wyższej | 
| Przetwarzanie zamówienia | Realizacja zakupu synchronicznie lub asynchronicznie | 

## 2. Opis technologii

Projekt został napisany w języku **Java 21** z wykorzystaniem narzędzia **Maven** do zarządzania zależnościami.

### Użyte technologie i biblioteki:

* **Java 21** – wersja języka użyta w projekcie.

* **Maven** – do budowania projektu i zarządzania zależnościami.

* **JUnit 5 & Mockito** – do pisania testów jednostkowych i mockowania zależności.

* **BigDecimal** – do dokładnych obliczeń finansowych (ceny i rabaty), co zapobiega błędom zaokrągleń.

* **CompletableFuture & ExecutorService** – do asynchronicznego przetwarzania zamówień.

### Mechanizmy programistyczne:

1. **Programowanie Obiektowe (OOP):** Wykorzystanie klas, polimorfizmu i abstrakcji do podziału odpowiedzialności (np. osobne klasy dla koszyka, produktów i serwisów).

2. **Wstrzykiwanie zależności (Dependency Injection):** Przekazywanie serwisów przez konstruktor (np. `DiscountService` wstrzykiwany do `OrderService`).

## 3. Przetwarzanie zamówień (Synchroniczne vs Asynchroniczne)

W systemie proces tworzenia zamówienia wywołuje metodę `createOrder` w `OrderService` (która symuluje czasochłonną operację). Może być on wywołany na dwa sposoby:

1. **Synchronicznie (`SyncOrderService`):** Wywołanie następuje bezpośrednio w głównym wątku, a aplikacja czeka na zakończenie przetwarzania.

2. **Asynchronicznie (`AsyncOrderService`):** Przetwarzanie delegowane jest do osobnego wątku przy użyciu `CompletableFuture.supplyAsync` i `ExecutorService`.

### Fragment kodu z projektu:

```java
// Synchroniczne przetwarzanie zamówienia
public class SyncOrderService {

    private final OrderService orderService;

    public SyncOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order processOrderSync(Customer customer, Cart cart) {
        return orderService.createOrder(customer, cart);
    }
}

// Asynchroniczne przetwarzanie zamówienia w tle (CompletableFuture)
public class AsyncOrderService {

    private final OrderService orderService;
    private final ExecutorService executorService;

    public AsyncOrderService(OrderService orderService, ExecutorService executorService) {
        this.orderService = orderService;
        this.executorService = executorService;
    }

    public CompletableFuture<Order> processOrderAsync(Customer customer, Cart cart) {
        return CompletableFuture.supplyAsync(
                () -> orderService.createOrder(customer, cart),
                executorService
        );
    }
}
```