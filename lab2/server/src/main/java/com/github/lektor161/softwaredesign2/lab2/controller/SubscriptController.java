package com.github.lektor161.softwaredesign2.lab2.controller;

import com.github.lektor161.softwaredesign2.lab2.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/manager-admin/subscriptions")
public class SubscriptController {

    private final ManagerService managerService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addSubscript(@RequestParam String clientName, @RequestParam int days) {
        managerService.addSubscript(clientName, days);
    }
}
