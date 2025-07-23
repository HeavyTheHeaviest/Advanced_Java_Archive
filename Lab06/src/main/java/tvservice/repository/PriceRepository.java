package tvservice.repository;

import tvservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price,Long> {
    List<Price> findByTypeAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            SubscriptionType type, LocalDate date1, LocalDate date2);
}
