package org.example.ecommercesystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    @NotBlank(message = "ID cannot be empty")
    private String ID; //Todo: ID validation

    @NotBlank(message = "name cannot be empty")
    @Size(min = 4, message = "name must be at least '4' in length")
    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "name must only contain characters")
    private String name;

    @NotNull(message = "price cannot be empty")
    @Positive(message = "price must be a positive number")
    private double price;

    @NotBlank(message = "categoryID cannot be empty")
    private String categoryID;
}
