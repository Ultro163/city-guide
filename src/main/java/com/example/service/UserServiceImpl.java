package com.example.service;

import com.example.model.EntityName;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет операции CRUD для пользователей.
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl extends AbstractCrudService<User, Long> {

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository, EntityName.USER);
    }

    @Override
    public User create(final User user) {
        return super.create(user);
    }

    @Override
    public User getById(final Long userId) {
        return super.getById(userId);
    }

    @Override
    public User update(final Long userId, final User updateUser) {
        final User user = getById(userId);
        Optional.ofNullable(updateUser.getName()).ifPresent(user::setName);
        Optional.ofNullable(updateUser.getEmail()).ifPresent(user::setEmail);
        return super.update(userId, user);
    }

    @Override
    public void delete(final Long userId) {
        super.delete(userId);
    }
}