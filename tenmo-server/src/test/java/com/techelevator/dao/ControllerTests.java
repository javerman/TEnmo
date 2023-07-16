package com.techelevator.dao;

import com.techelevator.tenmo.controller.AccountController;
import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.controller.UserController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ControllerTests {
    @Autowired
    UserController userController;
    @Autowired
    TransferController transferController;
    @Autowired
    AccountController accountController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        System.out.println("setup()...");
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

//    @Test
//    public void create
}
