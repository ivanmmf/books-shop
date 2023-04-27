package ru.maxima.booksshop.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@TestConfiguration
public class MockWebSessionConfig {
    @Bean
    @Primary
    public HttpServletRequest httpServletRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + "aXZhbl9tbWY6MTIz");
        return request;
    }
}
