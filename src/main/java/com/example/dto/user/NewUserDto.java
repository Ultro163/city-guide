package com.example.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания нового пользователя.
 * Этот класс используется для передачи данных, которые необходимы для создания нового пользователя в системе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @NotNull
    @Size(min = 2, max = 250)
    private String name;
    @NotNull
    @Size(min = 6, max = 254)
    @Email
    private String email;
}