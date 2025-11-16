package org.example.ecommercesystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    @NotBlank(message = "ID cannot be empty")
    private String ID;

    @NotBlank(message = "username cannot be empty")
    @Size(min = 6, message = "username have to be more than '6' in length")
    @Pattern(regexp = "^\\S+$") //No whitespace
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 7, message = "password have to be more than '7' in length")
    private String password; //Todo: Password validation


    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)$") //Todo: Check 'role' meaning
    private String role;

    @NotNull(message = "balance cannot be empty")
    @Positive(message = "balance must be a positive number")
    private double balance;
}
