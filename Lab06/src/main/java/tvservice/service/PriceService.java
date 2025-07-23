package tvservice.service;

import tvservice.model.Price;
import tvservice.model.SubscriptionType;
import tvservice.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepo;

    public Price addPrice(SubscriptionType type, double amount, LocalDate from) {
        Price p = new Price();
        p.setType(type); p.setAmount(amount);
        p.setValidFrom(from); p.setValidTo(null);
        return priceRepo.save(p);
    }

    public List<Price> allPrices() {
        return priceRepo.findAll();
    }

    /** Znajdź obowiązującą cenę dla danego typu i daty */
    public double currentPrice(SubscriptionType type, LocalDate onDate) {
        return priceRepo.findAll().stream()
                .filter(p -> p.getType() == type)
                .filter(p -> !p.getValidFrom().isAfter(onDate))
                .filter(p -> p.getValidTo() == null || !p.getValidTo().isBefore(onDate))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Brak cennika dla " + type + " na " + onDate))
                .getAmount();
    }

    // … metody do deaktywacji/correkt historii …
}
