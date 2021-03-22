package com.vertage.library.service;

import com.vertage.library.entity.Book;
import com.vertage.library.entity.User;
import com.vertage.library.exception.BookNotFoundException;
import com.vertage.library.exception.InvalidRequestDataException;
import com.vertage.library.exception.UserNotFoundException;
import com.vertage.library.repository.BookRepository;
import com.vertage.library.service.impl.BookServiceImpl;
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
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    private Book book;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookServiceImpl(bookRepository, userService);
        book = new Book(1L, "book1", null);
    }

    @Test
    public void getByIdSuccessCase() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertEquals(book, bookService.getById(book.getId()));
    }

    @Test()
    void getByIdWhenDoestExist() {
        when(bookRepository.findById(book.getId())).thenThrow(BookNotFoundException.class);

        assertThrows(BookNotFoundException.class, () -> bookService.getById(book.getId()));
    }

    @Test()
    void getByIdWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.getById(null));
    }

    @Test
    void getAllSuccessCase() {
        Book book2 = new Book(2L, "book2", null);
        List<Book> expectedBooks = Arrays.asList(book, book2);

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        assertEquals(expectedBooks.size(), bookService.getAll().size());
    }

    @Test
    void deleteByIdSuccessCase() {
        bookService.deleteById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test()
    void deleteByIdWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.deleteById(null));
    }

    @Test
    void saveSuccessCase() {
        when(bookRepository.save(book)).thenReturn(book);

        assertEquals(book, bookService.save(book));
    }

    @Test()
    void saveWhenBookIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.save(null));
    }

    @Test
    void takeBookSuccessCase() {
        Book spyBook = spy(book);
        User user = new User();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(spyBook));
        when(userService.getById(1L)).thenReturn(user);

        bookService.takeBook(1L, 1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(userService, times(1)).getById(1L);
        verify(spyBook, times(1)).setUser(user);
    }

    @Test
    void takeBookWhenBookNotFound() {
        User user = new User();

        when(bookRepository.findById(1L)).thenThrow(BookNotFoundException.class);

        assertThrows(BookNotFoundException.class, () -> bookService.takeBook(1L, 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(userService, times(0)).getById(1L);
    }

    @Test()
    void takeBookWhenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.takeBook(null, 1L));
    }

    @Test()
    void takeBookWhenBookIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.takeBook(1L, null));
    }

    @Test
    void takeBookWhenUserNotFound() {
        User user = new User();
        Book spyBook = spy(book);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(spyBook));
        when(userService.getById(1L)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> bookService.takeBook(1L, 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(userService, times(1)).getById(1L);
        verify(spyBook, times(0)).setUser(user);
    }

    @Test
    void takeBookWhenBookTaken() {
        User user = new User();
        book.setUser(user);
        Book spyBook = spy(book);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(spyBook));

        assertThrows(InvalidRequestDataException.class, () -> bookService.takeBook(1L, 1L));


        verify(bookRepository, times(1)).findById(1L);
        verify(userService, times(0)).getById(1L);
        verify(spyBook, times(0)).setUser(user);
    }

    @Test
    void returnBookSuccessCase() {
        Book spyBook = spy(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(spyBook));

        bookService.returnBook(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(spyBook, times(1)).setUser(null);
    }

    @Test()
    void returnBookWhenBookIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.returnBook(null));
    }

    @Test
    void returnBookWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenThrow(BookNotFoundException.class);

        assertThrows(BookNotFoundException.class, () -> bookService.returnBook(1L));

        verify(bookRepository, times(1)).findById(1L);
    }
}






