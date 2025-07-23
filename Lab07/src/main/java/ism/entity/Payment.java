package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name="subscription_id")
    private Subscription subscription;
    private LocalDate paymentDate;
    private Double amount;
}