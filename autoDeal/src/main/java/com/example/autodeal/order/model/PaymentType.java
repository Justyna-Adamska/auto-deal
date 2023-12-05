package com.example.autodeal.order.model;

public enum PaymentType {
    ONLINE, // Całkowita płatność online
    IN_STORE, // Całkowita płatność w sklepie
    DEPOSIT, // Zaliczka 20% wpłacona online
    PARTIAL_IN_STORE // Częściowa płatność w sklepie - reszta kwoty po odliczeniu zaliczki
}