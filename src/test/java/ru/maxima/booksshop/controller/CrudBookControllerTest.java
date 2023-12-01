package ru.maxima.booksshop.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxima.booksshop.exception.BookNotFoundException;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.BookRepository;
import ru.maxima.booksshop.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxima.booksshop.service.BookService;
import ru.maxima.booksshop.service.BookServiceImpl;

import static org.hamcrest.Matchers.*;


@WebMvcTest(CrudBookController.class)
class CrudBookControllerTest {


    @MockBean
    private BookService service;              // внедряем бин сервиса, который будем мокать
    @Autowired
    private MockMvc mockMvc;



    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void getAllBooks() throws Exception {
        given(service.getAllBooks())          // метод getAllProducts() возвращает пустой список
                .willReturn(List.of());

        mockMvc.perform(get("/api/v1/books")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                // вызываем метод контроллера
                // 3. Проверка метода
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void createBook() throws Exception {

        Book book = new Book();
        book.setId(150);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
        //when(service.save(book)).thenReturn(book);
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123"))
                        .with(csrf()))

                .andExpect(status().isOk());
                verify(service).save(ArgumentMatchers.refEq(book));
                //.andExpect(content().json(objectMapper.writeValueAsString(book)));
                //.andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void getBook() throws Exception {
        int id = 100;
        var book = Book.builder()
                .id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        //Mockito.when(service.findById(100)).thenReturn(Optional.ofNullable(book));
        given(service.findById(id))
                .willReturn(book);
        mockMvc.perform(get("/api/v1/book/100")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("War and Peace"));
    }

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void getNotFoundBook() throws Exception {
        int id = 2;
        var book = Book.builder()
                .id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        given(service.findById(id))
                .willThrow(new BookNotFoundException());
        mockMvc.perform(get("/api/v1/book/2")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isNotFound());
    }




    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void update() throws Exception {
        int id = 200;
        Book book = new Book();
        book.setId(id);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
//        given(service.findById(id))
//                .willReturn(book);
        var book1 = Book.builder()
                .id(200)
                .author("Dick Waterspoon")
                .name("Java!!")
                .iban("67765")
                .build();

        mockMvc.perform(put("/api/v1/book/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123"))
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(service).update(ArgumentMatchers.eq(200),ArgumentMatchers.refEq(book1));

    }

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void failUpdate() throws Exception {
        int id = 200;
        Book book = new Book();
        book.setId(id);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");

        var book1 = Book.builder()
                .id(200)
                .author("Dick Waterspoon")
                .name("Java!!")
                .iban("67765")
                .build();
        //doThrow(new BookNotFoundException()).when(service).update(5000,book);
             doThrow(new BookNotFoundException()).when(service).update(ArgumentMatchers.eq(5000),ArgumentMatchers.refEq(book1));

        mockMvc.perform(put("/api/v1/book/5000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123"))
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }


    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void deleteBook() throws Exception {
        Book book = new Book();
        book.setId(200);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
        Mockito.when(service.findById(200)).thenReturn(book);
        mockMvc.perform(delete("/api/v1/book/200")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123"))
                .with(csrf()))
                .andExpect(status().isOk());
                verify(service).delete(ArgumentMatchers.refEq(book));

    }

    @Test
    @WithMockUser(username = "ivan_mmf", password = "123")
    void getBookByAuthor() throws Exception {
        String author = "Lev";
        var book = Book.builder()
                .id(300)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var book1 = Book.builder()
                .id(350)
                .author("Lev Tolstoy")
                .name("War")
                .iban("61246")
                .build();

        Mockito.when(service.findByAuthorStartingWith("Lev")).thenReturn(List.of(book,book1));

        mockMvc.perform(get("/api/v1/books/author/Lev")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect( jsonPath("$.[0].author",is("Lev Tolstoy")))
                .andExpect( jsonPath("$.[1].author",is("Lev Tolstoy")));

    }
}