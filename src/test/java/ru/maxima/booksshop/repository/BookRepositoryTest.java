package ru.maxima.booksshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;        // для записи сущностей в БД используем entityManager


    @Test
    void findByAuthorStartingWith() {
        Book book1 = Book.builder()
                //.id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("654865")
                .build();

        Book book2 = Book.builder()
                //.id(200)
                .author("Dmitry Chub")
                .name("Java Introduction")
                .iban("786543")
                .build();
        Book book3 = Book.builder()
                //.id(300)
                .author("Lev Yashin")
                .name("Football")
                .iban("884865")
                .build();
//        productRepository.save(product1);
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);

        // 2. Выполнение метода (проверка функциональности)
        List<Book> books= bookRepository.findByAuthorStartingWith("Lev");

        // 3. Проверка метода

        assertThat(books).hasSize(2);
        assertThat(books.get(0).getName()).isEqualTo("War and Peace");
        assertThat(books.get(1).getName()).isEqualTo("Football");
    }
}