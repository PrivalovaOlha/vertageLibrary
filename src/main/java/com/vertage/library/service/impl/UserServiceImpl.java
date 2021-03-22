package com.vertage.library.service.impl;

import com.vertage.library.entity.User;
import com.vertage.library.exception.UserNotFoundException;
import com.vertage.library.repository.UserRepository;
import com.vertage.library.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(@NonNull Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User save(@NonNull User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(@NonNull Long userId) {
        userRepository.deleteById(userId);
    }

}
