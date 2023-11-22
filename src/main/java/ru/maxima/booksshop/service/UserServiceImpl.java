package ru.maxima.booksshop.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.UserRepository;

import java.util.List;

@Service    // создаём бин userService
@Transactional(readOnly = true) // помечаем методы класса только на чтение
public class UserServiceImpl implements UserService{

    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired  // внедряем бин userRepository
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
        System.out.println("**********Userservice******************");
    }

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();    // используем специальный метод findAll() из userRepository
    }

    @Override
    @Transactional // здесь аннотация помечаем метод на запись
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);  // метод save() добавляет пользователя, если его не было и изменяет, если уже был
    }


}
