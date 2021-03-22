package com.vertage.library.service;

import com.vertage.library.entity.User;
import com.vertage.library.exception.UserNotFoundException;
import com.vertage.library.repository.UserRepository;
import com.vertage.library.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "name", null);
    }

    @Test
    void getByIdSuccessCase() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(user, userService.getById(user.getId()));
    }

    @Test()
    void getByIdWhenUserNotFound() {
        when(userRepository.findById(user.getId())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> userService.getById(user.getId()));
    }

    @Test()
    void getByIdWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> userService.getById(null));
    }

    @Test
    void getAllSuccessCase() {
        User user2 = new User(2L, "user2", null);
        List<User> expectedUsers = Arrays.asList(user, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        assertEquals(expectedUsers.size(), userService.getAll().size());
    }

    @Test
    void deleteByIdSuccessCase() {
        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test()
    void deleteByIdWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> userService.deleteById(null));
    }

    @Test
    void saveSuccessCase() {
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.save(user));
    }

    @Test()
    void saveWhenBookIsNull() {
        assertThrows(NullPointerException.class, () -> userService.save(null));
    }
}
