package ism.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity @Data @NoArgsConstructor
@Table(name="clients")
public class Client {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String clientNumber;
    @OneToMany(mappedBy="client", cascade=CascadeType.ALL)
    private List<Subscription> subscriptions;
}
