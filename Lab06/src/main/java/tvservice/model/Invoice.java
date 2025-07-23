// Invoice.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
public class Invoice {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Subscription subscription;

    private LocalDate dueDate;
    private double amount;
    private boolean paid;
}
