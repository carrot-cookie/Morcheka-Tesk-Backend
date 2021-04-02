package org.example.entity.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginModel {
    @NotEmpty(message = "login must be not empty")
    private String login;
    @NotEmpty(message = "password must be not empty")
    private String password;
}
