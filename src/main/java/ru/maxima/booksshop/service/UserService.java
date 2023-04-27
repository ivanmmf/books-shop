package ru.maxima.booksshop.service;

import ru.maxima.booksshop.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    void saveUser(User user);

}
