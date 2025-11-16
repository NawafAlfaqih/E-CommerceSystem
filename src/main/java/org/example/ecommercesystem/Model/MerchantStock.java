package org.example.ecommercesystem.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotBlank(message = "ID cannot be empty")
    private String ID;

    @NotBlank(message = "productID cannot be empty")
    private String productID;

    @NotBlank(message = "merchantID cannot be empty")
    private String merchantID;

    @NotNull(message = "stock cannot be empty")
    @Positive(message = "stock must be a positive number")
    @Min(value = 10, message = "stock value must be at least '10' ")
    private int stock;

}
