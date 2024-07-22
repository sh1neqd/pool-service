package ru.alexbogdan.pool_service.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alexbogdan.pool_service.models.User;
import ru.alexbogdan.pool_service.models.dtos.UserDTO;
import ru.alexbogdan.pool_service.services.UserService;


@RestController
@RequestMapping("/api/v0/pool/client/")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addClient(@Valid @RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllClients() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClient(@RequestParam Long id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateClient(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
}
