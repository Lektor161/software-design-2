package com.github.lektor161.softwaredesign2.lab2.controller;

import com.github.lektor161.softwaredesign2.lab2.model.DayStatistic;
import com.github.lektor161.softwaredesign2.lab2.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report-service")
public class StatisticsController {

    private final StatisticService statisticService;

    @RequestMapping(value = "/stat-by-last-days", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DayStatistic> statisticsByLastDays(@RequestParam String userName, @RequestParam int lastDays) {
        return statisticService.getStatictic(userName, lastDays);
    }
}
