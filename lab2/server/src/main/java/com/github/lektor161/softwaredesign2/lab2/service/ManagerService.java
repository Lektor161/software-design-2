package com.github.lektor161.softwaredesign2.lab2.service;

import com.github.lektor161.softwaredesign2.lab2.repository.SubscriptionRepository;
import com.github.lektor161.softwaredesign2.lab2.model.Client;
import com.github.lektor161.softwaredesign2.lab2.model.Subscription;
import com.github.lektor161.softwaredesign2.lab2.repository.ClientRepository;
import com.github.lektor161.softwaredesign2.lab2.util.MyClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ManagerService {
    private final ClientRepository clientRepository;

    private final SubscriptionRepository clientEventRepository;

    public void addSubscript(String name, int days) {
        if (clientRepository.getAllByUserName(name).isEmpty()) {
            throw new IllegalArgumentException();
        }
        Subscription subscriptionEvent = new Subscription();
        subscriptionEvent.setUserName(name);
        subscriptionEvent.setDays(days);
        subscriptionEvent.setDate(MyClock.getTime().toLocalDate());
        clientEventRepository.save(subscriptionEvent);
    }

    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    public Client getClient(String name) {
        var clients = clientRepository.getAllByUserName(name);
        if (clients.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return clients.get(0);
    }

    public void addClient(String name) {
        if (!clientRepository.getAllByUserName(name).isEmpty()) {
            throw new IllegalArgumentException();
        }
        Client client = new Client();
        client.setUserName(name);
        clientRepository.save(client);
    }
}
