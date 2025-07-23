package processors;

import reflectionapi.Processor;
import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeProcessor implements Processor {
    @Override
    public String getInfo() {
        return "waluta: miasto, kantor1=wartosc1, kantor2=wartosc2 ...";
    }

    @Override
    public String process(String task) {
        // Oczekiwany format zadania: "City, Office1=Rate1, Office2=Rate2, ..."
        String[] parts = task.split(",");
        if (parts.length < 2) {
            return "Błąd: niepoprawny format zadania. Format: 'City, Office1=Rate1, Office2=Rate2,...'";
        }
        String city = parts[0].trim();
        Map<String, Double> offers = new HashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String[] pair = parts[i].split("=");
            if (pair.length != 2) {
                return "Błąd: niepoprawny format oferty " + parts[i];
            }
            String office = pair[0].trim();
            double rate;
            try {
                rate = Double.parseDouble(pair[1].trim());
            } catch (NumberFormatException ex) {
                return "Błąd: niepoprawna wartość kursu w: " + parts[i];
            }
            offers.put(office, rate);
        }
        if (offers.isEmpty()) {
            return "Brak ofert kursowych.";
        }
        // Wybieramy najlepszą ofertę – przyjmujemy, że najlepsza oferta to ta z najwyższym kursem
        String bestOffice = null;
        double bestRate = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, Double> entry : offers.entrySet()) {
            if (entry.getValue() > bestRate) {
                bestRate = entry.getValue();
                bestOffice = entry.getKey();
            }
        }
        return String.format("Dla miasta %s najlepszą ofertę ma kantor '%s' z kursem %.2f", city, bestOffice, bestRate);
    }
}

