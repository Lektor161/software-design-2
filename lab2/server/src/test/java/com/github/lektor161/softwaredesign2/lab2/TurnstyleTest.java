package com.github.lektor161.softwaredesign2.lab2;

import com.github.lektor161.softwaredesign2.lab2.service.ManagerService;
import com.github.lektor161.softwaredesign2.lab2.service.TurnstyleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TurnstyleTest extends AbstractClockTest {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private TurnstyleService turnstyleService;

    @Test
    public void testWithout() {
        String userName = "test-user";
        assertThrows(IllegalArgumentException.class, () -> turnstyleService.enter(userName));

        managerService.addClient(userName);
        assertThat(turnstyleService.enter(userName), is(false));

        managerService.addSubscript(userName, 1);
        assertThat(turnstyleService.enter(userName), is(true));

        setTime(DATE.plusDays(2));
        assertThat(turnstyleService.enter(userName), is(false));
    }

    @Test
    public void testTwoEnter() {
        String userName = "test-user2";
        managerService.addClient(userName);
        managerService.addSubscript(userName, 1);
        assertThat(turnstyleService.enter(userName), is(true));
        assertThat(turnstyleService.enter(userName), is(false));
    }

    @Test
    public void testExtension() {
        String userName = "test-user3";
        managerService.addClient(userName);
        managerService.addSubscript(userName, 1);
        assertThat(turnstyleService.enter(userName), is(true));

        setTime(DATE.atTime(LocalTime.of(5, 0)));
        assertThat(turnstyleService.exit(userName), is(true));

        setTime(DATE.plusDays(10));
        managerService.addSubscript(userName, 1);
        assertThat(turnstyleService.enter(userName), is(true));
    }

    @Test
    public void testExtension2() {
        String userName = "test-user4";
        managerService.addClient(userName);
        managerService.addSubscript(userName, 5);
        assertThat(turnstyleService.enter(userName), is(true));

        setTime(DATE.atTime(LocalTime.of(5, 0)));
        assertThat(turnstyleService.exit(userName), is(true));

        setTime(DATE.plusDays(6));
        managerService.addSubscript(userName, 2);
        assertThat(turnstyleService.enter(userName), is(true));
    }
}
