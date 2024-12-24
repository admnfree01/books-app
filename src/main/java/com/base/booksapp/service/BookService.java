package com.base.booksapp.service;

import com.base.booksapp.model.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    Book getBookById(Long id);
    List<Book> getAllBooks();
}
