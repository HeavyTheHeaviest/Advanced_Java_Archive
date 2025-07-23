package com.example.ticketing.model;

public record Ticket(String id, String category, int number) {
    @Override
    public String toString() {
        return id;
    }

    public static Ticket of(TicketCategory cat, int number) {
        String id = cat.getSymbol() + number;
        return new Ticket(id, cat.getSymbol(), number);
    }
}
