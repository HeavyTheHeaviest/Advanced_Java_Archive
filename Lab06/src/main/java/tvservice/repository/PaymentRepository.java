package tvservice.repository;

import tvservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findBySubscriptionId(Long subId);
}
