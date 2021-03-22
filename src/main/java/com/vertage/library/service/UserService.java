package com.vertage.library.service;

import com.vertage.library.entity.User;
import lombok.NonNull;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(@NonNull Long userId);

    User save(@NonNull User user);

    void deleteById(@NonNull Long userId);

}
