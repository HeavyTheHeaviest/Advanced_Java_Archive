package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity @Data @NoArgsConstructor
@Table(name="subscriptions")
public class Subscription {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name="client_id")
    private Client client;
    @Enumerated(EnumType.STRING)
    private ism.model.SubscriptionType type;
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(mappedBy="subscription", cascade=CascadeType.ALL)
    private List<Subaccount> subaccounts;
    @OneToMany(mappedBy="subscription", cascade=CascadeType.ALL)
    private List<Invoice> invoices;
    @OneToMany(mappedBy="subscription", cascade=CascadeType.ALL)
    private List<Payment> payments;
}