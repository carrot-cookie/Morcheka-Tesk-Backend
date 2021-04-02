package org.example.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditCredentialsModel {
    @NotNull
    private UUID id;
    @NotBlank
    private String login;
    private String rawPassword;
}
