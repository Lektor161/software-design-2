package com.github.lektor161.softwaredesign2.lab2.controller;

import com.github.lektor161.softwaredesign2.lab2.model.Client;
import com.github.lektor161.softwaredesign2.lab2.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1/manager-admin/clients")
public class ClientController {

    private final ManagerService managerService;

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Client> listClients() {
        return managerService.listClients();
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Client getClient(@RequestParam String name) {
        return managerService.getClient(name);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void addClient(@RequestParam String name) {
        managerService.addClient(name);
    }
}
