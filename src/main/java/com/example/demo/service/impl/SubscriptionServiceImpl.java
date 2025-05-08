package com.example.demo.service.impl;

import com.example.demo.model.Subscription;
import com.example.demo.model.User;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl extends BaseService<Subscription, Long> implements SubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    protected JpaRepository<Subscription, Long> getRepository() {
        return subscriptionRepository;
    }

    @Override
    @Transactional
    public Subscription addSubscription(Long userId, Subscription subscription) {
        log.info("Добавление новой подписки для пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        subscription.setUser(user);
        subscription.setStartDate(LocalDate.now());
        Subscription created = super.create(subscription);
        log.info("Создана новая подписка ID: {} для сервиса {}", created.getId(), created.getServiceName());
        return created;
    }

    @Override
    @Transactional
    public List<Subscription> getUserSubscriptions(Long userId) {
        log.info("Запрос списка подписок пользователя ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("Попытка запроса подписок несуществующего пользователя ID: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        log.debug("Найдено {} подписок для пользователя ID: {}", subscriptions.size(), userId);
        return subscriptions;
    }

    @Override
    @Transactional
    public void deleteSubscription(Long subscriptionId) {
        log.info("Удаление подписки ID: {}", subscriptionId);
        if (!subscriptionRepository.existsById(subscriptionId)) {
            log.error("Попытка удаления несуществующей подписки ID: {}", subscriptionId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Подписка не найдена");
        }

        super.delete(subscriptionId);
        log.info("Подписка ID: {} успешно удалена", subscriptionId);
    }

    @Override
    @Transactional
    public List<String> getTop3PopularSubscriptions() {
        log.info("Запрос ТОП-3 популярных подписок");
        List<String> topServices = subscriptionRepository.findTop3PopularServices()
                .stream()
                .map(arr -> (String) arr[0])
                .collect(Collectors.toList());

        log.debug("ТОП-3 подписок: {}", topServices);
        return topServices;
    }
}