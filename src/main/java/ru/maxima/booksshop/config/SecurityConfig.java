package ru.maxima.booksshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@ComponentScan("ru.maxima.booksshop")
@Configuration      // поскольку это конфигурационный файл
@EnableWebSecurity  // для того, чтобы работал Spring Security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired      // внедряем бин authProvider
    private AuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {  // описываем разделение доступа к различным хендлерам
        var corsConfig = new CorsConfiguration().applyPermitDefaultValues();
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        http
                //.addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .csrf().disable()
                .cors().configurationSource(request -> corsConfig)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/**").hasRole("admin")
                .antMatchers("/api/v2/**").hasAnyRole("user", "admin")
                .anyRequest()
                .authenticated()// адреса для авторизованных пользователей
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // конфигурируется AuthenticationProvider
        auth.authenticationProvider(authProvider);                                  // предоставляем authProvider
    }




}