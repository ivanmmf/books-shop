package ru.maxima.booksshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.maxima.booksshop.DTO.UpdateUserBook;
import ru.maxima.booksshop.dao.impl.JpaUserBookDaoImpl;
import ru.maxima.booksshop.model.Book;
//import ru.maxima.booksshop.model.UserBook;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.BookRepository;

import javax.validation.Valid;
import java.util.List;
@Validated
@RestController
@RequestMapping("/api/v2/")                     // указываем версию API приложения
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/search-employee"})


// разрешаем React приложению обращаться к SpringBoot приложению
public class CrudUserBookController {

    @Autowired
    // @Qualifier("jpaUserBookDaoImpl")    // <--- Для смены реализации DAO менять тут
    private JpaUserBookDaoImpl userBookDao;

    @Autowired
    private BookRepository bookRepository;

//    public List<Book> findBooksByUserId(int id){
//        return userBookDao.findUserBooksByUserId(id);
//
//    }

    @GetMapping("/books")   // GET метод, который будет возвращать список книг в React приложение
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping("/user-book")
    public void addBook(@RequestBody @Valid UpdateUserBook updateUserBook) {

        String login = updateUserBook.getLogin();
        User user = userBookDao.findUserByLogin(login);

        int bookId = updateUserBook.getBook_id();
        Book book = userBookDao.findBookById(bookId);

        userBookDao.addBook(user, book);
    }

    @GetMapping("/user-book/{login}")   // GET метод, который будет возвращать список работников в React приложение
    public List<Book> getUserBook(@PathVariable String login) {
        return userBookDao.findUserBooksByLogin(login);
    }

    @DeleteMapping("/user-book")
    @Transactional(readOnly = false)
    public void deleteBook(@RequestBody UpdateUserBook updateUserBook) {
        String login = updateUserBook.getLogin();
        User user = userBookDao.findUserByLogin(login);
        int bookId = updateUserBook.getBook_id();
        Book book = userBookDao.findBookById(bookId);
        userBookDao.deleteBookFromUser(user,book);

    }
}
