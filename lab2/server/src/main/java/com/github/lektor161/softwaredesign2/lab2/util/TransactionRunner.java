package com.github.lektor161.softwaredesign2.lab2.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionRunner {
    @Transactional
    public void doInTransaction(Runnable r) {
        r.run();
    }
}
