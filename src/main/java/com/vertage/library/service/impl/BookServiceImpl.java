package com.vertage.library.service.impl;

import com.vertage.library.entity.Book;
import com.vertage.library.entity.User;
import com.vertage.library.exception.BookNotFoundException;
import com.vertage.library.exception.InvalidRequestDataException;
import com.vertage.library.repository.BookRepository;
import com.vertage.library.service.BookService;
import com.vertage.library.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    @NonNull
    private final BookRepository bookRepository;
    @NonNull
    private final UserService userService;

    @Override
    public void returnBook(@NonNull Long bookId) {
        Book book = getById(bookId);
        book.setUser(null);
    }

    @Override
    public void takeBook(@NonNull Long bookId, @NonNull Long userId) {
        Book book = getById(bookId);
        if (book.getUser() != null) {
            throw new InvalidRequestDataException("The book already taken. Please, input correct id");
        }
        User user = userService.getById(userId);
        book.setUser(user);
    }

    @Override
    public Book save(@NonNull Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(@NonNull Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Book getById(@NonNull Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}
