package com.vertage.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertage.library.entity.Book;
import com.vertage.library.exception.BookNotFoundException;
import com.vertage.library.exception.InvalidRequestDataException;
import com.vertage.library.exception.UserNotFoundException;
import com.vertage.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Book book;

    private static final String URI_BOOKS = "/books/";
    private static final String MEDIATYPE_JSON = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    @BeforeEach
    void setUp() {
        book = new Book(1L, "name", null);
    }

    @Test
    void constructorNegativeTest() {
        assertThrows(NullPointerException.class, () -> new BookController(null));
    }

    @Test
    void getAllSuccessCase() throws Exception {
        Book book2 = new Book(2L, "book2", null);
        List<Book> expected = Arrays.asList(book, book2);
        String expectedResponse = objectMapper.writeValueAsString(expected);

        when(bookService.getAll()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get(URI_BOOKS)
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getAll();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getByIdSuccessCase() throws Exception {
        String expectedResponse = objectMapper.writeValueAsString(book);

        when(bookService.getById(1L)).thenReturn(book);

        MvcResult mvcResult = mockMvc.perform(get(URI_BOOKS + 1L)
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getById(1L);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getByIdWhenBookNotFound() throws Exception {
        when(bookService.getById(1L)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(get(URI_BOOKS + 1L)
                .contentType(MEDIATYPE_JSON))
                .andExpect(status().isNotFound()).andReturn();

        verify(bookService, times(1)).getById(1L);
    }

    @Test
    void createSuccessCase() throws Exception {
        String expectedResponse = objectMapper.writeValueAsString(book);

        when(bookService.save(book)).thenReturn(book);

        MvcResult mvcResult = mockMvc.perform(post(URI_BOOKS)
                .contentType(MEDIATYPE_JSON)
                .content(expectedResponse))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).save(book);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteSuccessCase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI_BOOKS + 1L))
                .andExpect(status().isOk()).andReturn();

        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    void updateSuccessCase() throws Exception {

        String expectedResponse = objectMapper.writeValueAsString(book);
        when(bookService.save(book)).thenReturn(book);

        MvcResult mvcResult = mockMvc.perform(put(URI_BOOKS + 1L)
                .contentType(MEDIATYPE_JSON)
                .content(expectedResponse))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).save(book);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void takeBookByUserSuccessCase() throws Exception {
        mockMvc.perform(patch(URI_BOOKS + 1L + "/take")
                .param("userId", "1"))
                .andExpect(status().isOk()).andReturn();

        verify(bookService, times(1)).takeBook(1L, 1L);
    }

    @Test
    void takeBookWhenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(bookService).takeBook(1L, 1L);

        mockMvc.perform(patch(URI_BOOKS + 1L + "/take")
                .param("userId", "1"))
                .andExpect(status().isNotFound()).andReturn();

        verify(bookService, times(1)).takeBook(1L, 1L);
    }

    @Test
    void takeBookWhenBookNotFound() throws Exception {
        doThrow(new BookNotFoundException()).when(bookService).takeBook(1L, 1L);

        mockMvc.perform(patch(URI_BOOKS + 1L + "/take")
                .param("userId", "1"))
                .andExpect(status().isNotFound()).andReturn();

        verify(bookService, times(1)).takeBook(1L, 1L);
    }

    @Test
    void takeBookWhenBookTaken() throws Exception {
        doThrow(new InvalidRequestDataException()).when(bookService).takeBook(1L, 1L);

        mockMvc.perform(patch(URI_BOOKS + 1L + "/take")
                .param("userId", "1"))
                .andExpect(status().isBadRequest()).andReturn();

        verify(bookService, times(1)).takeBook(1L, 1L);
    }

    @Test
    void returnBookByUserSuccessCase() throws Exception {
        mockMvc.perform(patch(URI_BOOKS + 1L + "/return"))
                .andExpect(status().isOk()).andReturn();

        verify(bookService, times(1)).returnBook(1L);
    }

    @Test
    void returnBookWhenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(bookService).returnBook(1L);

        mockMvc.perform(patch(URI_BOOKS + 1L + "/return"))
                .andExpect(status().isNotFound()).andReturn();

        verify(bookService, times(1)).returnBook(1L);
    }
}
