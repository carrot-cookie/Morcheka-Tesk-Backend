package org.example.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.example.exception.JwtAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;


/**
 * Выполняет проверку токена и его полей. Достает из базы аккаунт и передает его в контекст
 */
public class JwtAuthManager implements AuthenticationManager {
    private final JwtParser jwsParser;

    public JwtAuthManager(String signKey) {
        this.jwsParser = Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(signKey.getBytes(), "HmacSHA256"))
                .build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthToken authToken = (JwtAuthToken) authentication;
        Jws<Claims> claims;
        try {
            claims = jwsParser.parseClaimsJws(authToken.getCredentials());
        } catch (Exception e) {
            throw new JwtAuthenticationException("JWT parse error");
        }
        authToken.setPrincipal(UUID.fromString(claims.getBody().getSubject()));
        authToken.setAuthenticated(true);
        return authToken;
    }
}
