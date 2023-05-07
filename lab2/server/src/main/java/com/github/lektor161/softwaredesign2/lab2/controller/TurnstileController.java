package com.github.lektor161.softwaredesign2.lab2.controller;

import com.github.lektor161.softwaredesign2.lab2.service.TurnstyleService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/turnstile")
public class TurnstileController {
    private final TurnstyleService turnstyleService;

    @RequestMapping(path = "/in", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    boolean in(@RequestParam String userName) {
        return turnstyleService.enter(userName);
    }

    @RequestMapping(path = "/out", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    boolean out(@RequestParam String userName) {
        return turnstyleService.exit(userName);
    }
}