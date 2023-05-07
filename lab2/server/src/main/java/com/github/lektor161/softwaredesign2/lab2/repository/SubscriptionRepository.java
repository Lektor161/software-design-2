package com.github.lektor161.softwaredesign2.lab2.repository;

import com.github.lektor161.softwaredesign2.lab2.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> getAllBySubscriptionEventId(Long subscriptionEventId);
    List<Subscription> getAllByUserName(String userName);
}
