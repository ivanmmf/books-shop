package ru.maxima.booksshop.service;

import ru.maxima.booksshop.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book findById(int id);
    List<Book>findByAuthorStartingWith(String author);
    void save(Book book);

    void delete(Book book);

    void update (int id, Book book);
}
