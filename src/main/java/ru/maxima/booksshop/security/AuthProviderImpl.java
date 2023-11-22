package ru.maxima.booksshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component  // создаём бин authProviderImpl
public class AuthProviderImpl implements AuthenticationProvider {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired  // внедряем бин userRepository
    public AuthProviderImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override   // метод обрабатывает данные на аутентификацию email and password from sign_in.ftl
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();    // получаем параметр login
        User user = repository.findByLogin(login);  // пытаемся достать пользователя по login
        if (user == null) {
            // смотрим нашёлся ли пользователь
            throw new UsernameNotFoundException("User not found");  // если пользователя нет, то выбрасываем исключение
        }
        String password = authentication.getCredentials().toString();   // если есть, то береём пароль из формы
        if (!password.equals(user.getPassword()))//(!passwordEncoder.matches(password,user.getPassword())) {                     // сравниваем пароль из формы с паролем из базы
        {  throw new BadCredentialsException("Bad credentials");       // если не совпадают, то выбрасываем исключение
        }

        List<GrantedAuthority> authorities = new ArrayList<>();         // пользователи, прошедшие аутентификацию
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return new UsernamePasswordAuthenticationToken(login, password, authorities);    // пользователь авторизован
    }

    @Override
    public boolean supports(Class<?> authentication) {                                       // служебный метод
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

