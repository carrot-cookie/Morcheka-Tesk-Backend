package org.example.config.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.UUID;

/**
 * Токен аутентификации. Используется для хранения данных в процессе аутентификации.
 * Если все прошло гладко - уходит дальше в контекст.
 */
public class JwtAuthToken extends AbstractAuthenticationToken {
    private final String token;
    private UUID accountID;

    public JwtAuthToken(String token) {
        super(null);
        this.accountID = null;
        this.token = token;
    }

    public void setPrincipal(UUID accountID) {
        this.accountID = accountID;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public UUID getPrincipal() {
        return accountID;
    }
}
