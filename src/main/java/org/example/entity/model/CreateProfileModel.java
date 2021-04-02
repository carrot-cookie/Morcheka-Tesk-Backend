package org.example.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProfileModel {
    @NotEmpty
    private String login;
    @NotEmpty
    private String rawPassword;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    private String email;
    @NotNull
    private LocalDate dateOfBirth;
}
