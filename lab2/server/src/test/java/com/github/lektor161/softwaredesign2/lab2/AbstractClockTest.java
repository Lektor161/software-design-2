package com.github.lektor161.softwaredesign2.lab2;

import com.github.lektor161.softwaredesign2.lab2.util.MyClock;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
public class AbstractClockTest {
    public static LocalDate DATE = LocalDate.of(2022, 1, 1);

    private static MockedStatic<MyClock> mock = Mockito.mockStatic(MyClock.class);
    @BeforeEach
    public void before() {
        setTime(DATE);
    }

    public static void setTime(LocalDateTime time) {
        mock.when(MyClock::getTime).thenReturn(time);
    }

    public static void setTime(LocalDate time) {
        mock.when(MyClock::getTime).thenReturn(time.atTime(LocalTime.of(0, 0)));
    }

}
