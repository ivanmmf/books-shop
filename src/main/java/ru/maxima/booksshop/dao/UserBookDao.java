package ru.maxima.booksshop.dao;

import org.springframework.stereotype.Repository;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.model.UserBook;

import java.util.List;
@Repository
public interface UserBookDao {
    List<Book> findUserBooksByLogin(String login);
    void addBook(User user, Book book);
}
