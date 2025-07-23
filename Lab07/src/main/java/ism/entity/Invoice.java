package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
@Table(name="invoices")
public class Invoice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name="subscription_id")
    private Subscription subscription;
    private LocalDate dueDate;
    private Double amount;
    private Boolean paid;
}