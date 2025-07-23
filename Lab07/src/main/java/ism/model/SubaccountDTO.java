package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SubaccountDTO {
    private Long id;
    @NotNull private Long subscriptionId;
    @NotBlank private String login;
    @NotBlank private String password;
}