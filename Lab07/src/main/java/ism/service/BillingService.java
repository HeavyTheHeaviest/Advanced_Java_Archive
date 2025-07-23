package ism.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ism.entity.Invoice;
import ism.entity.Subscription;
import ism.repository.InvoiceRepository;
import ism.repository.SubscriptionRepository;
import ism.util.SimulationClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class BillingService {
    private final SubscriptionRepository subRepo;
    private final InvoiceRepository invRepo;
    private final PriceService priceService;
    private final SimulationClock clock;

    private static final Logger monitorLog    = LogManager.getLogger("MonitorLogger");
    private static final Logger escalationLog = LogManager.getLogger("EscalationLogger");

    @Transactional
    public void generateMonthlyInvoices() {
        LocalDate today = clock.today();
        List<Subscription> subs = subRepo.findAll(); // filtrowanie aktywnych wg potrzeb
        for(var s: subs) {
            double price = priceService.currentPrice(s.getType(), today);
            Invoice inv = new Invoice();
            inv.setSubscription(s);
            inv.setDueDate(today.plusDays(14));
            inv.setAmount(price);
            inv.setPaid(false);
            invRepo.save(inv);
        }
    }

    @Transactional
    public void sendReminders() {
        LocalDate today = clock.today();
        invRepo.findByDueDateBeforeAndPaid(today,false).forEach(inv ->
                monitorLog.info("Reminder: faktura {} (sub {}) przeterminowana, kwota={}",
                        inv.getId(),
                        inv.getSubscription().getId(),
                        inv.getAmount()));
    }

    @Transactional
    public void escalate() {
        LocalDate threshold = clock.today().minusDays(30);
        invRepo.findByDueDateBeforeAndPaid(threshold,false).forEach(inv ->
                escalationLog.info("Eskalacja: faktura {} (sub {}) >30 dni nieopłacona",
                        inv.getId(),
                        inv.getSubscription().getId()));
    }
}