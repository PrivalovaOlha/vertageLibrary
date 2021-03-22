package com.vertage.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertage.library.entity.User;
import com.vertage.library.exception.UserNotFoundException;
import com.vertage.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private static final String URI_USERS = "/users/";
    private static final String MEDIATYPE_JSON = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";


    @Test
    void getAllSuccessCase() throws Exception {
        User user = new User(1L, "user1", null);
        User user2 = new User(2L, "user2", null);
        List<User> users = Arrays.asList(user, user2);

        String jsonUsers = objectMapper.writeValueAsString(users);

        when(userService.getAll()).thenReturn(users);

        MvcResult mvcResult = mockMvc.perform(get(URI_USERS)
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).getAll();
        String responseAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(jsonUsers, responseAsString);
    }

    @Test
    void getByIdSuccessCase() throws Exception {
        User user = new User(1L, "user1", null);
        String expectedUser = objectMapper.writeValueAsString(user);

        when(userService.getById(user.getId())).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get(URI_USERS + user.getId())
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isOk()).andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();

        verify(userService, times(1)).getById(user.getId());

        assertEquals(expectedUser, responseAsString);
    }

    @Test
    void getByIdWhenUserNotFound() throws Exception {
        when(userService.getById(1L)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get(URI_USERS + 1L)
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isNotFound()).andReturn();

        verify(userService, times(1)).getById(1L);
    }

    @Test
    void createSuccessCase() throws Exception {
        User user = new User();
        user.setName("user1");
        String expectedUser = objectMapper.writeValueAsString(user);

        when(userService.save(user)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(post(URI_USERS)
                .contentType(MEDIATYPE_JSON)
                .content(expectedUser))
                .andExpect(status().isOk()).andReturn();
        String userInResponse = mvcResult.getResponse().getContentAsString();

        verify(userService, times(1)).save(user);

        assertEquals(expectedUser, userInResponse);
    }

    @Test
    void deleteSuccessCase() throws Exception {
        mockMvc.perform(delete(URI_USERS + 1L))
                .andExpect(status().isOk()).andReturn();

        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    void updateSuccessCase() throws Exception {
        User user = new User(1L, "user1", null);
        String expectedUser = objectMapper.writeValueAsString(user);

        when(userService.save(user)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(put(URI_USERS + 1L)
                .contentType(MEDIATYPE_JSON)
                .content(expectedUser))
                .andExpect(status().isOk())
                .andReturn();
        String userInResponse = mvcResult.getResponse().getContentAsString();

        verify(userService, times(1)).save(user);

        assertEquals(expectedUser, userInResponse);
    }
}
