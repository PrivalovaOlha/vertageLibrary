package com.vertage.library.service;

import com.vertage.library.entity.Book;
import lombok.NonNull;

import java.util.List;

public interface BookService {
    void returnBook(@NonNull Long bookId);

    void takeBook(@NonNull Long bookId,@NonNull Long userId);

    Book save(@NonNull Book book);

    void deleteById(@NonNull Long bookId);

    Book getById(@NonNull Long bookId);

    List<Book> getAll();

}
