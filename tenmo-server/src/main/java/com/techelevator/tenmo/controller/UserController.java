package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController

public class UserController {
    private JdbcUserDao userDao;

    public UserController(JdbcUserDao dao) {
        this.userDao = dao;
    }
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        List<User> users = userDao.getUsers();
        return users;
    }
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public  User getUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return user;
        }
    }
    @RequestMapping(value = "/users/transfers/to/{id}", method = RequestMethod.GET)
    public User getUserByTransferTo(@PathVariable int id) {
        User user = userDao.getUserByTransferTo(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return user;
        }
    }

    @RequestMapping(value = "/users/transfers/from/{id}", method = RequestMethod.GET)
    public User getUserByTransferFrom(@PathVariable int id) {
        User user = userDao.getUserByTransferFrom(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return user;
        }
    }

}
