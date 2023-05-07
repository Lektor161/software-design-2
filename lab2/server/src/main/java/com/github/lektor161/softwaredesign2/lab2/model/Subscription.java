package com.github.lektor161.softwaredesign2.lab2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subscriptionEventId;

    private String userName;

    private LocalDate date;

    private long days;
}
