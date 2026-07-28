package DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is Required")
    @Email(message="Invalid email format")
    private String email;
    @NotBlank(message="password is required")
    @Size(min=6, message = "password must be atleast of 6 charachters")
    private String password;
    private String firstName;
    private String lastName;
}
