package com.example.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    @Transactional
    public T create(T entity) {
        log.info("Создание новой сущности {}", entity.getClass().getSimpleName());
        return getRepository().save(entity);
    }

    @Transactional
    public T getById(ID id) {
        log.info("Получение {} с ID: {}", getRussianEntityName(), id);
        return getRepository().findById(id)
                .orElseThrow(() -> {
                    log.error("{} с ID {} не найден", getRussianEntityName(), id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @Transactional
    public T update(ID id, T entity) {
        log.info("Обновление {} с ID: {}", getRussianEntityName(), id);
        if (!getRepository().existsById(id)) {
            log.error("Попытка обновления несуществующего {} с ID {}", getRussianEntityName(), id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getRepository().save(entity);
    }

    @Transactional
    public void delete(ID id) {
        log.info("Удаление {} с ID: {}", getRussianEntityName(), id);
        if (!getRepository().existsById(id)) {
            log.warn("Попытка удаления несуществующего {} с ID {}", getRussianEntityName(), id);
        }
        getRepository().deleteById(id);
        log.debug("{} с ID {} успешно удален", getRussianEntityName(), id);
    }

    protected String getRussianEntityName() {
        String className = getClass().getSimpleName()
                .replace("Service", "")
                .replace("Impl", "");

        return switch (className) {
            case "User" -> "Пользователь";
            case "Subscription" -> "Подписка";
            default -> className;
        };
    }
}