package ru.maxima.booksshop.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.model.UserBook;
import ru.maxima.booksshop.repository.BookRepository;
import ru.maxima.booksshop.repository.UserRepository;

import javax.persistence.EntityManager;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class JpaUserBookDaoImplTest {

    @Autowired
    private JpaUserBookDaoImpl userBookDao;

    @Autowired
    private BookRepository bookDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findUserBooksByLogin() {
        var book = Book.builder()
                //.id(300)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var book1 = Book.builder()
               // .id(350)
                .author("Lev Tolstoy")
                .name("War")
                .iban("61246")
                .build();
        var user = User.builder()
                //.id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        var user1 = User.builder()
                //.id(100)
                .login("p10")
                .password("d10")
                .email("e10")
                .role("user")
                .build();
        var userbook = UserBook.builder()
                //.id(100)
                .book(book)
                .user(user)
                .build();

        var userbook1 = UserBook.builder()
                //.id(100)
                .book(book1)
                .user(user)
                .build();

        var book2 = Book.builder()
                //.id(300)
                .author("Lev Yashin")
                .name("Football")
                .iban("884865")
                .build();
        var userbook2 = UserBook.builder()
                //.id(100)
                .book(book2)
                .user(user1)
                .build();

        entityManager.persist(user);
        entityManager.persist(book1);
        entityManager.persist(book);
        entityManager.persist(book2);
        entityManager.persist(user1);
        entityManager.persist(userbook);
        entityManager.persist(userbook1);
        entityManager.persist(userbook2);

        List<Book> userBooks = userBookDao.findUserBooksByLogin(user.getLogin());

        assertThat(userBooks).hasSize(2);
        assertThat(userBooks.get(0).getName()).isEqualTo("War and Peace");
        assertThat(userBooks.get(1).getName()).isEqualTo("War");

    }

    @Test
    void addBook() {
        var book = Book.builder()
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var user = User.builder()
                //.id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        entityManager.persist(user);
        entityManager.persist(book);
        userBookDao.addBook(user,book);
        List<Book> userBooks = userBookDao.findUserBooksByLogin(user.getLogin());
        assertThat(userBooks).hasSize(1);
        assertThat(userBooks.get(0).getName()).isEqualTo("War and Peace");




    }

    @Test
    void findUserByLogin() {
        var user = User.builder()
                //.id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        var user1 = User.builder()
                //.id(100)
                .login("p10")
                .password("d10")
                .email("e10")
                .role("user")
                .build();
        entityManager.persist(user);
        entityManager.persist(user1);
       User userResult = userBookDao.findUserByLogin(user.getLogin());
       assertThat(userResult).isEqualTo(user);


    }

    @Test
    void findBookById() {
        var book = Book.builder()
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var book1 = Book.builder()
                .author("Lev Tolstoy")
                .name("War")
                .iban("61246")
                .build();
        bookDao.save(book);
        bookDao.save(book1);
//        bookDao.save(book);
//        bookDao.save(book1);
        Book bookResult = userBookDao.findBookById(book1.getId());
        assertThat(bookResult.getName()).isEqualTo("War");

    }



    @Test
    void deleteBookFromUser() {
        var book = Book.builder()
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var user = User.builder()
                //.id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        entityManager.persist(user);
        entityManager.persist(book);
        userBookDao.addBook(user,book);
        userBookDao.deleteBookFromUser(user,book);
        List<Book> userBooks = userBookDao.findUserBooksByLogin(user.getLogin());
        assertThat(userBooks).hasSize(0);
    }
}