package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private Long id;
    @NotNull private Long subscriptionId;
    @NotNull private LocalDate dueDate;
    @NotNull private Double amount;
    private Boolean paid;
}