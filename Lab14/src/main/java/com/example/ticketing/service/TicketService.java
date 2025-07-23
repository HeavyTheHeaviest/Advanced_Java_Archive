package com.example.ticketing.service;

import com.example.ticketing.model.Ticket;
import com.example.ticketing.model.TicketCategory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TicketService {
    // Lista kategorii
    private final List<TicketCategory> categories = Collections.synchronizedList(new ArrayList<>());
    // Numeratory dla każdej kategorii
    private final Map<String, AtomicInteger> sequenceMap = new ConcurrentHashMap<>();
    // Własna kolejka oczekujących biletów
    private final LinkedList<Ticket> queue = new LinkedList<>();
    // Przypisanie kategorii do stanowisk
    private final Map<Integer, Set<String>> deskCategories = new ConcurrentHashMap<>();
    // Aktualnie obsługiwane bilety (stanowisko → Ticket)
    private final Map<Integer, Ticket> currentServing = new ConcurrentHashMap<>();
    // Listenerzy zmian w kolejce
    private final List<TicketServiceListener> listeners = new CopyOnWriteArrayList<>();

    // --- Rejestracja listenerów ---
    public void addListener(TicketServiceListener l) {
        listeners.add(l);
    }
    public void removeListener(TicketServiceListener l) {
        listeners.remove(l);
    }
    private void notifyListeners() {
        for (var l : listeners) {
            l.onQueueChanged();
        }
    }

    // --- Zarządzanie kategoriami ---
    public synchronized void setCategories(List<TicketCategory> newCats) {
        categories.clear();
        categories.addAll(newCats);
        sequenceMap.clear();
        newCats.forEach(c -> sequenceMap.put(c.getSymbol(), new AtomicInteger(0)));
        queue.clear();
        currentServing.clear();
        notifyListeners();
    }

    public synchronized List<TicketCategory> getCategories() {
        return new ArrayList<>(categories);
    }

    public synchronized void setCategoryPriority(String symbol, int newPriority) {
        categories.stream()
                .filter(c -> c.getSymbol().equals(symbol))
                .findFirst()
                .ifPresent(c -> c.setPriority(newPriority));
        notifyListeners();
    }

    public synchronized void setDeskCategories(int deskId, List<String> symbols) {
        deskCategories.put(deskId, new HashSet<>(symbols));
        notifyListeners();
    }

    public synchronized List<String> getDeskCategories(int deskId) {
        return deskCategories.getOrDefault(deskId, Set.of())
                .stream().collect(Collectors.toList());
    }

    public synchronized Ticket issueTicket(String categorySymbol) {
        TicketCategory cat = categories.stream()
                .filter(c -> c.getSymbol().equals(categorySymbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nieznana kategoria: " + categorySymbol));
        int num = sequenceMap.get(categorySymbol).incrementAndGet();
        Ticket t = Ticket.of(cat, num);
        queue.add(t);
        notifyListeners();
        return t;
    }

    public synchronized Ticket fetchNextWithoutNotify(int deskId) {
        Set<String> allowed = deskCategories.getOrDefault(deskId, Set.of());
        Optional<Ticket> opt = queue.stream()
                .filter(t -> allowed.contains(t.category()))
                .min(Comparator
                        .comparingInt((Ticket t) -> {
                            int pri = categories.stream()
                                    .filter(c -> c.getSymbol().equals(t.category()))
                                    .findFirst()
                                    .map(TicketCategory::getPriority)
                                    .orElse(0);
                            return -pri;
                        })
                        .thenComparingInt(Ticket::number)
                );
        if (opt.isPresent()) {
            Ticket t = opt.get();
            queue.remove(t);
            currentServing.put(deskId, t);
            return t;
        } else {
            currentServing.remove(deskId);
            return null;
        }
    }

    public Ticket finishAndFetchNext(int deskId) {
        Ticket t = fetchNextWithoutNotify(deskId);
        notifyListeners();
        return t;
    }

    public synchronized List<String> getAllWaitingTickets() {
        return queue.stream()
                .map(Ticket::id)
                .collect(Collectors.toList());
    }

    public synchronized Ticket getCurrentTicket(int deskId) {
        return currentServing.get(deskId);
    }
}
