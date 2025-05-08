package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends BaseService<User, Long> implements UserService {
    private final UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Создание нового пользователя с email: {}", user.getEmail());
        user.setCreatedAt(LocalDate.now());
        User createdUser = super.create(user);
        log.info("Успешно создан пользователь ID: {}, Email: {}", createdUser.getId(), createdUser.getEmail());
        return createdUser;
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        log.debug("Запрос данных пользователя ID: {}", id);
        User user = super.getById(id);
        log.debug("Найден пользователь ID: {}, Имя: {}", user.getId(), user.getName());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        log.info("Обновление данных пользователя ID: {}", id);
        user.setId(id);
        User updatedUser = super.update(id, user);
        log.info("Данные пользователя ID: {} успешно обновлены", id);
        return updatedUser;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.warn("Инициировано удаление пользователя ID: {}", id);
        super.delete(id);
        log.info("Пользователь ID: {} успешно удален", id);
    }
}