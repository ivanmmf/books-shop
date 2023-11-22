package ru.maxima.booksshop.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.booksshop.dao.impl.JpaUserBookDaoImpl;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.model.UserBook;
import ru.maxima.booksshop.repository.BookRepository;
import ru.maxima.booksshop.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@DataJpaTest
class AuthProviderImplTest {
   @Autowired
    private UserRepository userRepository;
    @MockBean
    private JpaUserBookDaoImpl userBookDao;

    @Autowired
    private BookRepository bookDao;

    @Autowired
    private EntityManager entityManager;

    private AuthProviderImpl authProvider;

    private  PasswordEncoder passwordEncoder;




//    @BeforeEach
//    void setUp() {
//        //userRepository = Mockito.mock(UserRepository.class);
//       // authProvider = new AuthProviderImpl(userRepository, passwordEncoder);
//        User user = User.builder()
//                .id(100)
//                .login("p1")
//                .password("d1")
//                .email("e1")
//                .role("user")
//                .build();
//
//        entityManager.merge(user);
//
//    }

   // @Autowired
    private MockMvc mockMvc;

    @Test
   // @WithMockUser(username = "ivan_mmf", password = "123")
    void successAuthenticate() throws Exception {
        var book = Book.builder()
                //.id(300)
                .author("Lev Tolstoy")
                .name("War and Peace")
                .iban("6546")
                .build();
        User user = User.builder()
               // .id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();
        var userbook = UserBook.builder()
                //.id(100)
                .book(book)
                .user(user)
                .build();
//        var userbooks = new ArrayList<UserBook>();
//        userbooks.add(userbook);
//        user.setBooks(userbooks);
//        book.setUsers(userbooks);
//        entityManager.persist(user);
//        entityManager.persist(book);
//        entityManager.persist(userbook);

        Mockito.when(userBookDao.findUserBooksByLogin(user.getLogin())).thenReturn(List.of(book));
        mockMvc.perform(get("/api/v2/user-book/p1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("p1", "d1")))
                //.with(SecurityMockMvcRequestPostProcessors.httpBasic("ivan_mmf", "123")))
                .andExpect(status().isOk());

    }

    @Test
    void failAuthenticate() {

    }


}