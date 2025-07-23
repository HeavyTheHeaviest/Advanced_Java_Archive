// Subscription.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity @Data @NoArgsConstructor @ToString(exclude = {"subaccounts", "invoices", "payments"})
public class Subscription {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Client client;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private LocalDate startDate;
    private LocalDate endDate; // null = aktywny

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Subaccount> subaccounts;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
