package ru.maxima.booksshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.maxima.booksshop.exception.BadCredentials;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.UserRepository;
import ru.maxima.booksshop.service.UserServiceImpl;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api/v3/auth")
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/login"})
public class AuthController {       // создаём отдельный контроллер для аутентификации, авторизации, регистрации

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;



    @PostMapping("/sign_up")
    public void user(@RequestBody User user) {

        if (userRepository.findByLogin(user.getLogin()) != null) {

          throw new BadCredentials();
        }
        else userService.saveUser(user);
    }

    @GetMapping("/whoami")
    public String whoami(Principal principal) {

        return principal.getName();
    }
    @GetMapping("/myroles")
    public Collection<? extends GrantedAuthority> myroles(Authentication authentication) {

        return authentication.getAuthorities();
    }
}
