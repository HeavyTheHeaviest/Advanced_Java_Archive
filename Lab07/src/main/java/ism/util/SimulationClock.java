package ism.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class SimulationClock {
    private LocalDate currentDate = LocalDate.now();

    public LocalDate today() {
        return currentDate;
    }

    public void plusDays(int days) {
        currentDate = currentDate.plusDays(days);
    }
}