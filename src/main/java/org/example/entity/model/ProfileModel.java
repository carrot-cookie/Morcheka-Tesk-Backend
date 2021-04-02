package org.example.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.example.entity.Profile;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProfileModel {
    private UUID id;
    private String login;
    private String name;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;

    public ProfileModel(Profile profile) {
        this.login = profile.getLogin();
        this.name = profile.getName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        this.dateOfBirth = profile.getDateOfBirth();
        this.id = profile.getId();
    }
}
