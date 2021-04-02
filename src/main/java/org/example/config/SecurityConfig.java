package org.example.config;

import org.example.config.auth.JwtAuthFilter;
import org.example.config.auth.JwtAuthManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Value("${security.sign-key}")
    private String SIGN_KEY;

    @Override
    protected AuthenticationManager authenticationManager() {
        return new JwtAuthManager(SIGN_KEY);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.addFilterBefore(buildAuthFilter(), AnonymousAuthenticationFilter.class);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*");
    }

    private AbstractAuthenticationProcessingFilter buildAuthFilter() {
        // В этом листе записываются пути, которые фильтр будет игнорировать
        LinkedList<RequestMatcher> pathList = new LinkedList<>();
        // Авторизация
        pathList.add(new AntPathRequestMatcher("/auth/login", "POST"));

        NegatedRequestMatcher negatedRequestMatcher = new NegatedRequestMatcher(new OrRequestMatcher(pathList));
        return new JwtAuthFilter(negatedRequestMatcher, authenticationManager());
    }
}

