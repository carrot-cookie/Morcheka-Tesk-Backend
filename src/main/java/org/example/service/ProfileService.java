package org.example.service;

import org.example.entity.Profile;
import org.example.entity.model.CreateProfileModel;
import org.example.entity.model.EditCredentialsModel;
import org.example.entity.model.EditProfileDataModel;
import org.example.entity.model.ProfileModel;
import org.example.exception.LoginAlreadyExistsException;
import org.example.exception.ProfileNotFoundException;
import org.example.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProfileService {
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) throws LoginAlreadyExistsException {
        this.profileRepository = profileRepository; this.passwordEncoder = passwordEncoder;
        if (!this.profileRepository.existsByLogin("admin")) createDefaultProfile();
    }

    private void createDefaultProfile() throws LoginAlreadyExistsException {
        CreateProfileModel createProfileModel = new CreateProfileModel();
        createProfileModel.setLogin("admin");
        createProfileModel.setName("Default");
        createProfileModel.setLastName("User");
        createProfileModel.setDateOfBirth(LocalDate.now());
        createProfileModel.setRawPassword("12345");
        createNewProfile(createProfileModel);
    }

    public Profile getProfile(UUID id) throws ProfileNotFoundException {
        return profileRepository.findById(id).orElseThrow(ProfileNotFoundException::new);
    }

    public void createNewProfile(CreateProfileModel createModel) throws LoginAlreadyExistsException {
        if (profileRepository.existsByLogin(createModel.getLogin())) throw new LoginAlreadyExistsException();
        String passwordHash = passwordEncoder.encode(createModel.getRawPassword());
        Profile profile = new Profile(createModel.getLogin(), passwordHash, createModel.getName(), createModel.getLastName(),
                createModel.getDateOfBirth());
        if (createModel.getEmail() != null) profile.setEmail(createModel.getEmail());
        profileRepository.save(profile);
    }

    public void editProfile(EditProfileDataModel editModel) throws ProfileNotFoundException {
        Profile profile = profileRepository.findById(editModel.getId()).orElseThrow(ProfileNotFoundException::new);
        profile.setName(editModel.getName());
        profile.setLastName(editModel.getLastName());
        profile.setEmail(editModel.getEmail());
        profile.setDateOfBirth(editModel.getDateOfBirth());
        profileRepository.save(profile);
    }

    public void editCredentials(EditCredentialsModel editModel) throws ProfileNotFoundException {
        Profile profile = profileRepository.findById(editModel.getId()).orElseThrow(ProfileNotFoundException::new);
        profile.setLogin(editModel.getLogin());
        if (editModel.getRawPassword() != null) {
            profile.setPasswordHash(passwordEncoder.encode(editModel.getRawPassword()));
        }
        profileRepository.save(profile);
    }

    public Page<ProfileModel> getAllProfiles(Pageable pageable) {
        return profileRepository.findAllModels(pageable);
    }
}
