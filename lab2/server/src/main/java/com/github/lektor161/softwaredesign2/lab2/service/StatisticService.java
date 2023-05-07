package com.github.lektor161.softwaredesign2.lab2.service;

import com.github.lektor161.softwaredesign2.lab2.model.DayStatistic;
import com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent;
import com.github.lektor161.softwaredesign2.lab2.repository.TurnstyleEventRepository;
import com.github.lektor161.softwaredesign2.lab2.util.MyClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent.TurnstyleEventType.ENTER;
import static com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent.TurnstyleEventType.EXIT;

@RequiredArgsConstructor
@Service
public class StatisticService {
    private final Map<String, Map<LocalDate, DayStatistic>> statistics = new ConcurrentHashMap<>();

    private final Map<String, LocalDateTime> enters = new ConcurrentHashMap<>();

    public StatisticService(
            ManagerService managerService,
            TurnstyleEventRepository turnstyleEventRepository
    ) {
        for (var client: managerService.listClients()) {
            statistics.putIfAbsent(client.getUserName(), new ConcurrentHashMap<>());
            var events = turnstyleEventRepository.getByUserName(client.getUserName()).stream()
                    .sorted(Comparator.comparing(TurnstyleEvent::getTime))
                    .toList();
            for (int i = 0; i + 1 < events.size(); i++) {
                if (events.get(i).getType() == EXIT) {
                    continue;
                }
                var enterEvent = events.get(i);
                var exitEvent = events.get(i + 1);
                addTime(client.getUserName(),
                        enterEvent.getTime().toLocalDate(),
                        exitEvent.getTime().toEpochSecond(ZoneOffset.UTC) -
                                enterEvent.getTime().toEpochSecond(ZoneOffset.UTC)
                );
            }
        }
    }

    private void addTime(String userName, LocalDateTime enter, LocalDateTime exit) {
        addTime(
                userName,
                enter.toLocalDate(),
                exit.toEpochSecond(ZoneOffset.UTC) - enter.toEpochSecond(ZoneOffset.UTC)
        );
    }

    private void addTime(String userName, LocalDate date, long count) {
        statistics.putIfAbsent(userName, new HashMap<>());
        statistics.get(userName).putIfAbsent(date, new DayStatistic(date, 0, 0));
        statistics.get(userName).get(date).addDurationSum(count);
    }

    public void event(TurnstyleEvent event) {
        var userName = event.getUserName();
        if (event.getType() == ENTER) {
            enters.put(userName, event.getTime());
            return;
        }
        if (enters.containsKey(userName)) {
            addTime(userName, enters.get(userName), event.getTime());
        }
    }

    public List<DayStatistic> getStatictic(String userName, int days) {
        var curDate = MyClock.getTime().toLocalDate();
        List<DayStatistic> res = new ArrayList<>();

        var stat = statistics.get(userName);
        if (stat == null) {
            return res;
        }
        for (int day = 0; day < days; day++) {
            var date = curDate.minusDays(day);
            if (stat.containsKey(date)) {
                res.add(stat.get(date));
            }
        }
        return res;
    }
}
