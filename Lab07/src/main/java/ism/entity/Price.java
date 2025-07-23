package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
@Table(name="prices")
public class Price {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ism.model.SubscriptionType type;
    private Double amount;
    private LocalDate validFrom;
    private LocalDate validTo;
}