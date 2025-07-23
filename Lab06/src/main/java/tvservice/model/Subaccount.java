// Subaccount.java
package tvservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity @Data @NoArgsConstructor
public class Subaccount {
    @Id @GeneratedValue
    private Long id;
    private String login;
    private String password;

    @ManyToOne
    private Subscription subscription;
}
