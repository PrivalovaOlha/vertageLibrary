package com.vertage.library.controller;

import com.vertage.library.entity.User;
import com.vertage.library.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User get(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }

    @PutMapping("{id}")
    public User update(@PathVariable("id") Long id,
                       @RequestBody User user) {
        user.setId(id);
        return userService.save(user);
    }
}
