package com.sample.orders.integration;

import com.sample.common.domains.User;
import com.sample.orders.unit.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok().body(usersService.findUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findItem(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok().body(usersService.findUser(id));
    }
}
