package org.example.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.entity.Profile;
import org.example.entity.model.JwtModel;
import org.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;

@Service
public class JwtService {
    @Value("${security.sign-key}")
    private String SIGN_KEY;

    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    public JwtService(PasswordEncoder passwordEncoder, ProfileRepository profileRepository) {
        this.passwordEncoder = passwordEncoder; this.profileRepository = profileRepository;
    }

    private String generateFor(Profile profile) {
        return Jwts.builder()
                .setSubject(profile.getId().toString())
                .signWith(new SecretKeySpec(SIGN_KEY.getBytes(), "HmacSHA256"), SignatureAlgorithm.HS256)
                .compact();
    }

    public String login(String login, String password) throws AuthenticationException, AccountNotFoundException {
        Profile profile = profileRepository.findByLogin(login).orElseThrow(AccountNotFoundException::new);
        if (passwordEncoder.matches(password, profile.getPasswordHash())) {
            return generateFor(profile);
        } else throw new AuthenticationException("Password not match");
    }
}
