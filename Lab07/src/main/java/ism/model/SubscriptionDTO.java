package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SubscriptionDTO {
    private Long id;
    @NotNull private Long clientId;
    @NotNull private SubscriptionType type;
    @NotNull private LocalDate startDate;
    private LocalDate endDate;
}