// Price.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
public class Price {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;
    private double amount;
    private LocalDate validFrom;
    private LocalDate validTo;  // null = ciągle ważne
}
