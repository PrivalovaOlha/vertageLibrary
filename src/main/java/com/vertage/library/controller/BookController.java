package com.vertage.library.controller;

import com.vertage.library.entity.Book;
import com.vertage.library.service.BookService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    @NonNull
    private final BookService bookService;

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("{id}")
    public Book get(@PathVariable("id") Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.save(book);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("{id}")
    public Book update(@PathVariable("id") Long id,
                       @RequestBody Book book) {
        book.setId(id);
        return bookService.save(book);
    }

    @PatchMapping("{id}/take")
    public void takeBookByUser(@PathVariable("id") Long id,
                               @RequestParam("userId") Long userId) {
        bookService.takeBook(id, userId);
    }

    @PatchMapping("{id}/return")
    public void returnBookByUser(@PathVariable("id") Long id) {
        bookService.returnBook(id);
    }
}
