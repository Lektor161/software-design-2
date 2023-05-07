package com.github.lektor161.softwaredesign2.lab2.repository;

import com.github.lektor161.softwaredesign2.lab2.model.TurnstyleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurnstyleEventRepository extends JpaRepository<TurnstyleEvent, Long> {
    List<TurnstyleEvent> getByUserName(String userName);
}
