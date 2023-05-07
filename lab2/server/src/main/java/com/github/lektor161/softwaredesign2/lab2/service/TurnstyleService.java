package com.github.lektor161.softwaredesign2.lab2.service;

import com.github.lektor161.softwaredesign2.lab2.model.Subscription;
import com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent;
import com.github.lektor161.softwaredesign2.lab2.repository.ClientRepository;
import com.github.lektor161.softwaredesign2.lab2.repository.SubscriptionRepository;
import com.github.lektor161.softwaredesign2.lab2.repository.TurnstyleEventRepository;
import com.github.lektor161.softwaredesign2.lab2.util.MyClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent.TurnstyleEventType.ENTER;
import static com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent.TurnstyleEventType.EXIT;

@RequiredArgsConstructor
@Service
public class TurnstyleService {
    private final ClientRepository clientRepository;
    private final TurnstyleEventRepository turnstyleEventRepository;

    private final StatisticService statisticService;

    private final SubscriptionRepository subscriptionRepository;

    public boolean enter(String userName) {
        if (clientRepository.getAllByUserName(userName).isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (getLastType(userName).map(type -> type == ENTER).orElse(false)) {
            return false;
        }

        var now = MyClock.getTime();
        if (now.toLocalDate().isAfter(subscriptionExpireDate(userName))) {
            return false;
        }

        var event = new TurnstyleEvent();
        event.setUserName(userName);
        event.setTime(now.toLocalDate().atTime(now.toLocalTime()));
        event.setType(ENTER);
        turnstyleEventRepository.save(event);
        statisticService.event(event);

        return true;
    }

    public boolean exit(String userName) {
        if (clientRepository.getAllByUserName(userName).isEmpty()) {
            return false;
        }
        if (getLastType(userName).map(type -> type == EXIT).orElse(false)) {
            return false;
        }

        var now = MyClock.getTime();
        var event = new TurnstyleEvent();
        event.setUserName(userName);
        event.setTime(now.toLocalDate().atTime(now.toLocalTime()));
        event.setType(EXIT);
        event = turnstyleEventRepository.save(event);
        statisticService.event(event);
        return true;
    }

    private LocalDate subscriptionExpireDate(String userName) {
        LocalDate res = LocalDate.MIN;
        for (var sub: subscriptionRepository.getAllByUserName(userName).stream()
                .sorted(Comparator.comparing(Subscription::getDate))
                .toList()
        ) {
            if (sub.getDate().isAfter(res)) {
                res = sub.getDate();
            }
            res = res.plusDays(sub.getDays());
        }
        return res;
    }

    private Optional<TurnstyleEvent.TurnstyleEventType> getLastType(String userName) {
        return turnstyleEventRepository.getByUserName(userName).stream()
                .max(Comparator.comparing(TurnstyleEvent::getTime))
                .map(TurnstyleEvent::getType);
    }
}
