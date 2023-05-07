package com.github.lektor161.softwaredesign2.lab2.repository;

import com.github.lektor161.softwaredesign2.lab2.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> getAllByUserName(String userName);
}
