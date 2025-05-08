package com.example.demo.service;

import com.example.demo.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    Subscription addSubscription(Long userId, Subscription subscription);

    List<Subscription> getUserSubscriptions(Long userId);

    void deleteSubscription(Long subscriptionId);

    List<String> getTop3PopularSubscriptions();
}