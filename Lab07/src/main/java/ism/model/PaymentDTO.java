package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long id;
    @NotNull private Long subscriptionId;
    @NotNull private LocalDate paymentDate;
    @NotNull private Double amount;
}