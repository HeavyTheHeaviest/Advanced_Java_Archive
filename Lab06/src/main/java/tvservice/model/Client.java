// Client.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity @Data @NoArgsConstructor @ToString(exclude = "subscriptions")
public class Client {
    @Id @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String clientNumber;  // unikalny nr klienta

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;
}
