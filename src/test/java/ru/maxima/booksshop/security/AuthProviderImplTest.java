//package ru.maxima.booksshop.security;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.context.support.WithMockUser;
//import ru.maxima.booksshop.model.User;
//import ru.maxima.booksshop.repository.UserRepository;
//import ru.maxima.booksshop.service.UserServiceImpl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AuthProviderImplTest {
//    private UserRepository userRepository;
//
//    private AuthProviderImpl authProvider;
//
//
//    @BeforeEach
//    void setUp() {
//        userRepository = Mockito.mock(UserRepository.class);
//        authProvider = new AuthProviderImpl(userRepository);
//        User user = User.builder()
//                .id(100)
//                .login("p1")
//                .password("d1")
//                .email("e1")
//                .role("user")
//                .build();
//    }
//
//    @Test
//    @WithMockUser(username = "ivan_mmf", password = "123")
//    void successAuthenticate() {
//
//
//
//
//    }
//
//    @Test
//    void failAuthenticate() {
//
//    }
//
//
//}