package tvservice.service;

import tvservice.model.*;
import tvservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class PaymentService {
    public final PaymentRepository payRepo;
    private final InvoiceRepository invRepo;

    public Payment registerPayment(Long subscriptionId, LocalDate date, double amount) {
        Payment p = new Payment();
        p.setSubscription(new Subscription() {{ setId(subscriptionId); }});
        p.setPaymentDate(date);
        p.setAmount(amount);
        payRepo.save(p);
        // oznacz faktury jako zapłacone (upraszczając: w kolejności do kwoty)
        double remaining = amount;
        for (Invoice inv : invRepo.findByPaidFalse()) {
            if (!inv.getSubscription().getId().equals(subscriptionId)) continue;
            if (remaining >= inv.getAmount()) {
                remaining -= inv.getAmount();
                inv.setPaid(true);
                invRepo.save(inv);
            }
            if (remaining <= 0) break;
        }
        return p;
    }
}
