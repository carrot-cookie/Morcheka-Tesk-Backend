package org.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

@Data
@Entity
public class Profile {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();
    private String login;
    private String passwordHash;
    private String name;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;

    public Profile(String login, String passwordHash, String name, String lastName, LocalDate dateOfBirth) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    protected Profile() {

    }
}
