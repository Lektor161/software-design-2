package com.github.lektor161.softwaredesign2.lab2.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public class MyClock {
    public static LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
    }
}
