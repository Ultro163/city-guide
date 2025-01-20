package com.example.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления данных пользователя.
 * Этот класс используется для передачи данных, которые необходимы для обновления информации о пользователе в системе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @Size(min = 2, max = 250)
    private String name;
    @Size(min = 6, max = 254)
    @Email
    private String email;
}