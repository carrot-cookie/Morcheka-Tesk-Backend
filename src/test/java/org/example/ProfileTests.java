package org.example;

import org.example.entity.Profile;
import org.example.entity.model.CreateProfileModel;
import org.example.entity.model.EditCredentialsModel;
import org.example.entity.model.EditProfileDataModel;
import org.example.exception.LoginAlreadyExistsException;
import org.example.exception.ProfileNotFoundException;
import org.example.repository.ProfileRepository;
import org.example.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProfileTests {
    @InjectMocks
    ProfileService profileService;
    @Spy
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    ProfileRepository profileRepository;

    // Защифрованный "12345qwerty!"
    String PASSWORD_HASH = "$2y$10$UTvg9grp7ofqQe1xnN4Cde.pbQeNDHvzGbHApjS4bMRmpeTTLSE.e";

    public ProfileTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void profileCreation() throws LoginAlreadyExistsException {
        ArgumentCaptor<Profile> argumentCaptor = ArgumentCaptor.forClass(Profile.class);
        Profile profile = new Profile("vasya1", PASSWORD_HASH, "Vasya", "Pupkin",
                LocalDate.now());
        Mockito.when(profileRepository.existsByLogin("vasya1")).thenReturn(false);
        profileService.createNewProfile(new CreateProfileModel(profile.getLogin(), "12345qwerty!",
                profile.getName(), profile.getLastName(), profile.getEmail(), profile.getDateOfBirth()));
        Mockito.verify(profileRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Profile profile1 = argumentCaptor.getValue();
        assert profile.getName().equals(profile1.getName());
        assert profile.getLastName().equals(profile1.getLastName());
        assert profile.getDateOfBirth().equals(profile1.getDateOfBirth());
        assert profile1.getEmail() == null;
        assert passwordEncoder.matches("12345qwerty!", profile1.getPasswordHash());
    }

    @Test
    void editProfile() throws ProfileNotFoundException {
        LocalDate DOB = LocalDate.now();
        ArgumentCaptor<Profile> argumentCaptor = ArgumentCaptor.forClass(Profile.class);
        Profile profile = new Profile("vasya1", PASSWORD_HASH, "Vasya", "Pupkin", DOB);
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        profileService.editProfile(new EditProfileDataModel(profile.getId(),
                "Petya",
                "Noskov",
                "mail@mail.com",
                DOB));
        Mockito.verify(profileRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Profile profile1 = argumentCaptor.getValue();
        assert profile1.getName().equals("Petya");
        assert profile1.getLastName().equals("Noskov");
        assert profile1.getLogin().equals(profile.getLogin());
        assert profile1.getEmail().equals("mail@mail.com");
        assert profile1.getDateOfBirth().equals(DOB);
    }

    @Test
    void editCredentials() throws ProfileNotFoundException {
        ArgumentCaptor<Profile> argumentCaptor = ArgumentCaptor.forClass(Profile.class);
        Profile profile = new Profile("vasya1", PASSWORD_HASH, "Vasya", "Pupkin", LocalDate.now());
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        profileService.editCredentials(new EditCredentialsModel(profile.getId(), "petya4", "111"));
        Mockito.verify(profileRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Profile profile1 = argumentCaptor.getValue();
        assert profile1.getLogin().equals("petya4");
        assert passwordEncoder.matches("111", profile1.getPasswordHash());
    }

    @Test
    void getProfile() throws ProfileNotFoundException {
        Profile profile = new Profile("vasya1", PASSWORD_HASH, "Vasya", "Pupkin", LocalDate.now());
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        Profile profile1 = profileService.getProfile(profile.getId());
        assert profile1.getName().equals(profile.getName());
        assert profile1.getLastName().equals(profile.getLastName());
        assert profile1.getLogin().equals(profile.getLogin());
        assert profile1.getEmail() == null;
        assert profile1.getDateOfBirth().equals(profile.getDateOfBirth());
        assert profile1.getLogin().equals(profile.getLogin());
        assert passwordEncoder.matches("12345qwerty!", profile1.getPasswordHash());
    }
}
