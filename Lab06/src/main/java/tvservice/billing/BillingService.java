package tvservice.billing;

import tvservice.model.*;
import tvservice.repository.*;
import tvservice.service.PriceService;
import tvservice.util.SimulationClock;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class BillingService {
    private final SubscriptionRepository subRepo;
    public final InvoiceRepository invRepo;
    private final SimulationClock clock;
    private final PriceService priceService;
    private static final Logger monitorLog =
            LogManager.getLogger("MonitorLogger");
    private static final Logger escalationLog =
            LogManager.getLogger("EscalationLogger");

    /** Generuj faktury miesięczne dla wszystkich aktywnych subskrypcji */
    public void generateMonthlyInvoices() {
        LocalDate today = clock.today();
        subRepo.findAll().stream()
                .filter(s -> s.getEndDate()==null || !s.getEndDate().isBefore(today))
                .forEach(s -> {
                    Invoice inv = new Invoice();
                    inv.setSubscription(s);
                    inv.setDueDate(today.plusDays(14)); // 14 dni na zapłatę
                    double price = priceService.currentPrice(s.getType(), today);
                    inv.setAmount(price);
                    inv.setPaid(false);
                    invRepo.save(inv);
                });
    }

    /** Wysyłaj monity dla niezapłaconych faktur z terminem ≤ dziś */
    public void sendReminders() {
        LocalDate today = clock.today();
        List<Invoice> overdue = invRepo.findByDueDateBeforeAndPaid(today,false);
        overdue.forEach(inv -> {
            monitorLog.info("Reminder: faktura {} dla subskrypcji {} jest przeterminowana. Kwota: {}",
                    inv.getId(), inv.getSubscription().getId(), inv.getAmount());
        });
    }

    /** Eskalacja: jeśli >30 dni po terminie nadal nieopłacone */
    public void escalate() {
        LocalDate threshold = clock.today().minusDays(30);
        List<Invoice> list = invRepo.findByDueDateBeforeAndPaid(threshold,false);
        list.forEach(inv -> {
            escalationLog.info("Eskalacja: faktura {} dla sub {} nadal nieopłacona ponad 30 dni!",
                    inv.getId(), inv.getSubscription().getId());
        });
    }
}
