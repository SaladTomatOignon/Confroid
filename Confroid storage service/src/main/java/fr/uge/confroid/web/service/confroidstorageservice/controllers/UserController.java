package fr.uge.confroid.web.service.confroidstorageservice.controllers;

import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import fr.uge.confroid.web.service.confroidstorageservice.services.UserService;
import fr.uge.confroid.web.service.confroidstorageservice.utils.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return ResponseEntity.of(userService.getByUsername(username));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        userService.login(user.getUsername(), user.getPassword());

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody User user) {
        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
