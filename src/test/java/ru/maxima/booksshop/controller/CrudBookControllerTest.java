package ru.maxima.booksshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CrudBookControllerTest {
    @MockBean
    //@Autowired
    private BookRepository bookRepository;              // внедряем бин сервиса, который будем мокать

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void initUser() {
        String login = "ivan_mmf";
        var user = userRepository.findByLogin(login);
        var password = passwordEncoder.encode("123");
        if (user == null) {
            User testUser = new User();
            testUser.setLogin("ivan_mmf");
            testUser.setPassword(password);
            testUser.setRole("admin");
            userRepository.save(testUser);
        } else if (user.getPassword() == null) {
            user.setPassword(password);
            userRepository.save(user);
        }
    }


    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void findBookById() throws Exception {
        int id = 100;
        Book book = new Book();
        book.setId(100);
        book.setName("War and Peace");
        book.setAuthor("Lev Tolstoy");
        book.setIban("65465");
        given(bookRepository.findById(100))
                .willReturn(Optional.of(book));
        Book testBook = bookRepository.findById(100).orElseThrow(null);
        assertThat(testBook.getName()).isEqualTo("War and Peace");
    }

    @Test
        // @WithMockUser(username = "ivan_mmf", password = "123")
    void getAllBooks() throws Exception {
        given(bookRepository.findAll())          // метод getAllProducts() возвращает пустой список
                .willReturn(List.of());

        mockMvc.perform(get("/api/v1/books")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                // вызываем метод контроллера
                // 3. Проверка метода
                .andExpect(status().isOk());
    }

    @Test
    void createBook() throws Exception {

        Book book = new Book();
        book.setId(150);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(book);
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
                //.andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void getBook() throws Exception {
        int id = 100;
        var book = Book.builder()
                .id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
//        given(bookRepository.getReferenceById(id).getName())
//                .willReturn(String.valueOf(book));
        mockMvc.perform(get("/api/v1/book/100")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("War and Peace"));
    }


    @Test
    void update() throws Exception {
        Book book = new Book();
        book.setId(200);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(book);
        Mockito.when(bookRepository.findById(200)).thenReturn(Optional.ofNullable(book));
        var book1 = Book.builder()
                .id(200)
                .author("Dick Waterspoon")
                .name("Java!!")
                .iban("67765")
                .build();

        mockMvc.perform(put("/api/v1/book/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java!!"));
    }

    @Test
    void deleteBook() throws Exception {
        Book book = new Book();
        book.setId(200);
        book.setName("Java");
        book.setAuthor("Dick Waterspoon");
        book.setIban("67765");
        Mockito.when(bookRepository.findById(200)).thenReturn(Optional.ofNullable(book));
        mockMvc.perform(delete("/api/v1/book/200")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk());

    }

    @Test
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
        List<Book> resultBooks =  bookRepository.findByAuthorStartingWith("Lev");
        Mockito.when(bookRepository.findByAuthorStartingWith("Lev")).thenReturn(List.of(book,book1));

        mockMvc.perform(get("/api/v1/books/author/Lev")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect( jsonPath("$.[0].author",is("Lev Tolstoy")))
                .andExpect( jsonPath("$.[1].author",is("Lev Tolstoy")));


    }



}