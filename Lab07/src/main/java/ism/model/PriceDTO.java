package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PriceDTO {
    private Long id;
    @NotNull private SubscriptionType type;
    @NotNull private Double amount;
    @NotNull private LocalDate validFrom;
    private LocalDate validTo;
}