package org.example.config.auth;

import org.example.exception.JwtAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Фильтр, для авторизации по JWT токену. Проверяет каждый запрос
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthFilter(RequestMatcher requiresMatcher, AuthenticationManager authenticationManager) {
        super(requiresMatcher, authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // При успешной аутентификации - идем дальше
        this.setAuthenticationSuccessHandler((request1, response1, authentication) -> chain.doFilter(request1, response1));
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Проверяем что хедер "Authorization" имеет значение Bearer и токен
        String token;
        try {
            token = parseToken(request);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Unable to parse token");
        }
        JwtAuthToken authToken = new JwtAuthToken(token);
        // Аутентифицируем пользователя
        return this.getAuthenticationManager().authenticate(authToken);
    }

    private String parseToken(HttpServletRequest request) throws Exception  {
        String[] headerData = request.getHeader("Authorization").split(" ");
        if (!headerData[0].equals("Bearer")) throw new Exception();
        return headerData[1];
    }
}
