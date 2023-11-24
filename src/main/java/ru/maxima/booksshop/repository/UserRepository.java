package ru.maxima.booksshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.maxima.booksshop.model.User;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);


}
