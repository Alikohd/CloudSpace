package ru.oleevych.cloudspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotBlank
    @Size(min = 3, max = 24, message = "Допускается длина имени от 3 до 24")
    private String username;
    @NotBlank
    @Size(min = 5, max = 32, message = "Допускается длина пароля от 5 до 32")
    private String password;
    private String confirmPassword;
}
