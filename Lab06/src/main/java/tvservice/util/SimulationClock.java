// SimulationClock.java
package tvservice.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class SimulationClock {
    private LocalDate currentDate = LocalDate.now();

    public LocalDate today() {
        return currentDate;
    }

    /** Przesuń symulowany czas o N dni */
    public void plusDays(int days) {
        currentDate = currentDate.plusDays(days);
    }

    /** Ustaw nową datę symulacji */
    public void setDate(LocalDate date) {
        currentDate = date;
    }
}
