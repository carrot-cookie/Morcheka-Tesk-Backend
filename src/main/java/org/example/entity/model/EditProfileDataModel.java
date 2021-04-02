package org.example.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditProfileDataModel {
    @NotNull
    private UUID id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    private String email;
    @NotNull
    private LocalDate dateOfBirth;
}
