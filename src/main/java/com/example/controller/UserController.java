package com.example.controller;

import com.example.dto.mappers.UserMapper;
import com.example.dto.user.NewUserDto;
import com.example.dto.user.UpdateUserDto;
import com.example.dto.user.UserDto;
import com.example.model.User;
import com.example.service.CrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления пользователями.
 * Предоставляет API для создания, обновления, удаления и получения информации о пользователях.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final CrudService<User, Long> userService;
    private final UserMapper userMapper;

    /**
     * Создает нового пользователя.
     *
     * @param dto объект {@link NewUserDto}, содержащий данные для создания пользователя.
     * @return объект {@link UserDto}, представляющий полную информацию о созданном пользователе.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody final NewUserDto dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toUserDto(userService.create(user));
    }

    /**
     * Возвращает информацию о пользователе по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return объект {@link UserDto}, содержащий полную информацию о пользователе.
     */
    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable final long userId) {
        return userMapper.toUserDto(userService.getById(userId));
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param userId идентификатор пользователя.
     * @param dto    объект {@link UpdateUserDto}, содержащий обновленные данные пользователя.
     * @return объект {@link UserDto}, представляющий полную информацию об обновленном пользователе.
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable final long userId, @Valid @RequestBody final UpdateUserDto dto) {
        User user = userMapper.toEntity(dto);
        user.setId(userId);
        return userMapper.toUserDto(userService.update(userId, user));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final long userId) {
        userService.delete(userId);
    }
}