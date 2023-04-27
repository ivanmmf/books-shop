package ru.maxima.booksshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.mockito.BDDMockito.given;
@ActiveProfiles("test")
class UserServiceTest {

    private UserRepository userRepository;

    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return null;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };

    }

    @Test
    void getUsers() {
        // 1. Условия для метода
        User user1 = User.builder()
                .id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();

        User user2 = User.builder()
                .id(200)
                .login("p2")
                .password("d2")
                .email("e2")
                .role("admin")
                .build();
        given(userRepository.findAll())
                .willReturn(List.of(user1, user2));

        List<User> users = userService.getUsers();

        // 3. Проверка метода
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId()).isEqualTo(100);
        assertThat(users.get(0).getLogin()).isEqualTo("p1");
        assertThat(users.get(1).getId()).isEqualTo(200);
        assertThat(users.get(1).getLogin()).isEqualTo("p2");

    }
//        @Test
//        void saveUserCreate() {
//        User user3 = User.builder()
//                .id(148)
//                .login("p3")
//                .password("d3")
//                .email("e3")
//                .role("user")
//                .build();
//        given(userRepository.findByLogin("p3"))
//                    .willReturn(user3);
//
//        userService.saveUser(user3);
//        User user = userRepository.findByLogin("p3");
//
//        assertThat(user.getEmail()).isEqualTo("e3");
//
//        }
}

