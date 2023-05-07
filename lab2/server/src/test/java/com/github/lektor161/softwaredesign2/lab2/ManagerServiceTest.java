package com.github.lektor161.softwaredesign2.lab2;

import com.github.lektor161.softwaredesign2.lab2.service.ManagerService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ManagerServiceTest {

    @Autowired
    private ManagerService managerService;


    @Test
    void test() {
        String clientName = "TestName";
        MatcherAssert.assertThat(managerService.listClients(), is(empty()));
        assertThrows(IllegalArgumentException.class, () -> managerService.getClient(clientName));

        managerService.addClient(clientName);
        MatcherAssert.assertThat(managerService.listClients(), is(hasSize(1)));
        MatcherAssert.assertThat(managerService.getClient(clientName).getUserName(), is(clientName));

        assertThrows(IllegalArgumentException.class, () -> managerService.addClient(clientName));
    }

}
