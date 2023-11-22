package ru.maxima.booksshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.ResultMatcher;
import ru.maxima.booksshop.DTO.UpdateUserBook;
import ru.maxima.booksshop.dao.impl.JpaUserBookDaoImpl;
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
class CrudUserBookControllerTest {
    @MockBean
    //@Autowired
    private BookRepository bookRepository; // внедряем бин сервиса, который будем мокать
    @MockBean
    //@Autowired
    private JpaUserBookDaoImpl userBookDao;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void initUser() {
        String login = "ivan_mmf";
        var user = userRepository.findByLogin(login);
        var password = "123";
        if (user == null) {
            User testUser = new User();
            testUser.setLogin("ivan_mmf");
            testUser.setPassword(password);
            testUser.setRole("admin");
            testUser.setEmail("ivan.p.rybnikov@gmail.com");
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
    void addBook() throws Exception {

        UpdateUserBook userBook = new UpdateUserBook();
        userBook.setBook_id(200);
        userBook.setLogin("p1");
        var book = Book.builder()
                .id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
//        Mockito.doNothing().when(userBookDao).addBook(user,book);
//       Mockito.when(userBookDao.findUserByLogin("p1")).thenReturn(user);
        mockMvc.perform(post("/api/v2/user-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk());
                //.andExpect(content().json(objectMapper.writeValueAsString(book)));

    }

    @Test
    void getUserBook() throws Exception {
        var book = Book.builder()
                .id(100)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        var book1 = Book.builder()
                .id(150)
                .author("Lev Tolstoy")
                .name("War")
                .iban("777")
                .build();
        User user1 = User.builder()
                .id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        Mockito.when(userBookDao.findUserBooksByLogin(user1.getLogin())).thenReturn(List.of(book,book1));
        mockMvc.perform(get("/api/v2/user-book/p1")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("War"));

    }

    @Test
    void deleteBook() throws Exception {

        UpdateUserBook userBook = new UpdateUserBook();
        userBook.setBook_id(200);
        userBook.setLogin("p1");
        //Mockito.when(userBookDao.deleteBookFromUser(user,book)).thenReturn(Optional.ofNullable(book));
        mockMvc.perform(delete("/api/v2/user-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBook))
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk());
    }


}