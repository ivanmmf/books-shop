package ru.maxima.booksshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.maxima.booksshop.model.User;

import javax.persistence.EntityManager;


import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;        // для записи сущностей в БД используем entityManager

    @Test
    void findByLogin() {
        User user1 = User.builder()
                //.id(100)
                .login("p1")
                .password("d1")
                .email("e1")
                .role("user")
                .build();

        User user2 = User.builder()
                //.id(200)
                .login("p2")
                .password("d2")
                .email("e2")
                .role("admin")
                .build();


//        productRepository.save(product1);
        entityManager.persist(user1);
        entityManager.persist(user2);

        // 2. Выполнение метода (проверка функциональности)
        User user= userRepository.findByLogin("p1");

        // 3. Проверка метода
        assertThat(user.getLogin()).isEqualTo("p1");
    }
}




