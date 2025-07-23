package ism.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ClientDTO {
    private Long id;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank private String clientNumber;
}