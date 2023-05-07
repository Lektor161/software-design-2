package com.github.lektor161.softwaredesign2.lab2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayStatistic {
    private LocalDate date;
    private int numOfDurations;
    private long durationSum;

    public void addDurationSum(long count) {
        durationSum += count;
        numOfDurations++;
    }
}