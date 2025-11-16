package org.example.ecommercesystem.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {

    @NotBlank(message = "ID cannot be empty")
    private String ID;

    @NotBlank(message = "name cannot be empty")
    @Size(min = 4, message = "name must be at least '4' in length")
    @Pattern(regexp = "^[0-9a-zA-Z\s]+$", message = "name must only contain characters and numbers")
    private String name;

}
