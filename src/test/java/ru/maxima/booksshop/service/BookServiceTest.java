package ru.maxima.booksshop.service;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ru.maxima.booksshop.exception.BookNotFoundException;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.BookRepository;
import ru.maxima.booksshop.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")

public class BookServiceTest {
    private BookRepository bookRepository;
    private BookServiceImpl bookService;


    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void getAllBooks() {
        Book book1 = Book.builder()
                .id(1)
                .name("War")
                .author("Lev")
                .iban("123")
                .build();
        Book book2 = Book.builder()
                .id(2)
                .name("Peace")
                .author("Tolstoy")
                .iban("456")
                .build();
        BDDMockito.given(bookRepository.findAll()).willReturn(List.of(book1,book2));
        List<Book> books = bookService.getAllBooks();
        assertThat(books).hasSize(2);


    }

    @Test
    void findById() {
        Book book1 = Book.builder()
                .id(1)
                .name("War")
                .author("Lev")
                .iban("123")
                .build();
        Book book2 = Book.builder()
                .id(2)
                .name("Peace")
                .author("Tolstoy")
                .iban("456")
                .build();
        given(bookRepository.findById(1)).willReturn(Optional.ofNullable(book1));
        doThrow(BookNotFoundException.class).when(bookRepository)
                .findById(3);
        Optional<Book> book3 = Optional.ofNullable(bookService.findById(1));

        assertThat(book3.get().getName()).isEqualTo("War");
        assertThrows(BookNotFoundException.class,() -> bookRepository.findById(3));
    }

    @Test
    void findByAuthorStartingWith() {
        Book book1 = Book.builder()
                .id(1)
                .name("War")
                .author("Lev")
                .iban("123")
                .build();
        Book book2 = Book.builder()
                .id(2)
                .name("Peace")
                .author("Tolstoy")
                .iban("456")
                .build();
        BDDMockito.given(bookRepository.findByAuthorStartingWith("L")).willReturn(List.of(book1));
        List<Book> books = bookService.findByAuthorStartingWith("L");
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getName()).isEqualTo("War");
    }

    @Test
    void save() {
        Book book1 = Book.builder()
                .id(1)
                .name("War")
                .author("Lev")
                .iban("123")
                .build();
        when(bookRepository.save(book1)).thenReturn(book1);
        bookService.save(book1);
        verify(bookRepository).save(book1);
    }

    @Test
    void delete() {
        Book book1 = Book.builder()
                .id(1)
                .name("War")
                .author("Lev")
                .iban("123")
                .build();
        doNothing().when(bookRepository).delete(book1);
        bookService.delete(book1);

        verify(bookRepository, times(1)).delete(book1);
    }

}
