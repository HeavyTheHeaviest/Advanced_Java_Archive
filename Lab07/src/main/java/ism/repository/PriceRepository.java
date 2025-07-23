package ism.repository;

import ism.entity.Price;
import ism.model.SubscriptionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("""
    SELECT p FROM Price p
     WHERE p.type      = :type
       AND p.validFrom <= :date
       AND (p.validTo IS NULL OR p.validTo >= :date)
  """)
    List<Price> findCurrentPrices(@Param("type") SubscriptionType type,
                                  @Param("date") LocalDate date);
}