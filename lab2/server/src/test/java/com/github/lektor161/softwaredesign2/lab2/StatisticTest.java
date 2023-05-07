package com.github.lektor161.softwaredesign2.lab2;

import com.github.lektor161.softwaredesign2.lab2.model.DayStatistic;
import com.github.lektor161.softwaredesign2.lab2.repository.SubscriptionRepository;
import com.github.lektor161.softwaredesign2.lab2.service.ManagerService;
import com.github.lektor161.softwaredesign2.lab2.service.StatisticService;
import com.github.lektor161.softwaredesign2.lab2.service.TurnstyleService;
import com.github.lektor161.softwaredesign2.lab2.util.MyClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StatisticTest extends AbstractClockTest {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private TurnstyleService turnstyleService;

    @Autowired
    private StatisticService statisticService;

    @Test
    public void test() {
        var userName = "test-user";
        managerService.addClient(userName);
        managerService.addSubscript(userName, 10);

        setTime(DATE.atTime(1, 0));
        turnstyleService.enter(userName);
        setTime(DATE.atTime(2, 0));
        turnstyleService.exit(userName);
        setTime(DATE.atTime(3, 0));
        turnstyleService.enter(userName);
        setTime(DATE.atTime(3, 30));
        turnstyleService.exit(userName);

        var stats = statisticService.getStatictic(userName, 1);
        assertThat(stats, is(hasSize(1)));

        assertThat(stats.get(0).getDate(), is(DATE));
        assertThat(stats.get(0).getNumOfDurations(), is(2));
        assertThat(stats.get(0).getDurationSum(), is(90 * 60L));
    }
}
