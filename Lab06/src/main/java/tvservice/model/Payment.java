// Payment.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Subscription subscription;

    private LocalDate paymentDate;
    private double amount;
}
