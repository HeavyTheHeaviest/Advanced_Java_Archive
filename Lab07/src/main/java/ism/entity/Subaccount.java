package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity @Data @NoArgsConstructor
@Table(name="subaccounts")
public class Subaccount {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    @ManyToOne @JoinColumn(name="subscription_id")
    private Subscription subscription;
}