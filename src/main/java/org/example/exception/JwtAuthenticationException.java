package org.example.exception;

public class JwtAuthenticationException extends org.springframework.security.core.AuthenticationException {
    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
