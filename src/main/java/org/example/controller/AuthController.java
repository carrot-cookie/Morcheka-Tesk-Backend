package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entity.model.JwtModel;
import org.example.entity.model.LoginModel;
import org.example.service.JwtService;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController()
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private JwtService jwtService;
    
    @PostMapping("login")
    public JwtModel login(@Valid @RequestBody LoginModel loginModel, HttpServletResponse response)
            throws AuthenticationException, AccountNotFoundException {
        return new JwtModel(jwtService.login(loginModel.getLogin(), loginModel.getPassword()));
    }
}
