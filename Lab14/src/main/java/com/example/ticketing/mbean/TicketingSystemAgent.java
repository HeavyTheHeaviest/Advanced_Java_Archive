package com.example.ticketing.mbean;

import com.example.ticketing.model.TicketCategory;
import com.example.ticketing.service.TicketService;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TicketingSystemAgent extends NotificationBroadcasterSupport
        implements TicketingSystemAgentMBean {

    private final TicketService service;
    private long sequence = 1L;

    public TicketingSystemAgent(TicketService service) {
        this.service = service;
    }

    public void updateCategoriesInternal(String[] defs) {
        List<TicketCategory> cats = Arrays.stream(defs)
                .map(s -> {
                    var parts = s.split(":");
                    return new TicketCategory(parts[0], Integer.parseInt(parts[1]));
                }).collect(Collectors.toList());
        service.setCategories(cats);
    }

    public void updateDeskCategoriesInternal(int deskId, String[] symbols) {
        service.setDeskCategories(deskId, Arrays.asList(symbols));
    }


    @Override
    public String[] listCategories() {
        return service.getCategories().stream()
                .map(TicketCategory::getSymbol)
                .toArray(String[]::new);
    }

    @Override
    public void setCategories(String[] categoryDefs) {
        updateCategoriesInternal(categoryDefs);
        sendNotification(new Notification(
                "com.example.ticketing.categories.changed",
                this, sequence++,
                "Kategorie zostały zmienione przez MBean"
        ));
    }

    @Override
    public void setCategoryPriority(String categorySymbol, int priority) {
        service.setCategoryPriority(categorySymbol, priority);
        sendNotification(new Notification(
                "com.example.ticketing.priority.changed",
                this, sequence++,
                "Priorytet " + categorySymbol + " -> " + priority
        ));
    }

    @Override
    public String[] getDeskCategories(int deskId) {
        List<String> lst = service.getDeskCategories(deskId);
        return lst.toArray(new String[0]);
    }

    @Override
    public void setDeskCategories(int deskId, String csv) {
        String[] symbols = csv.split(",");
        service.setDeskCategories(deskId, Arrays.asList(symbols));
        sendNotification(new Notification(
                "com.example.ticketing.desk.categories.changed",
                this, sequence++,
                "Stanowisko " + deskId + " -> " + Arrays.toString(symbols)
        ));
    }

    @Override
    public String[] listCategoryPriorities() {
        return service.getCategories().stream()
                .map(c -> c.getSymbol() + ":" + c.getPriority())
                .toArray(String[]::new);
    }
}
