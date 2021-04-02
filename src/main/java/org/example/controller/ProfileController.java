package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entity.model.CreateProfileModel;
import org.example.entity.model.EditCredentialsModel;
import org.example.entity.model.EditProfileDataModel;
import org.example.entity.model.ProfileModel;
import org.example.exception.LoginAlreadyExistsException;
import org.example.exception.ProfileNotFoundException;
import org.example.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("new")
    public void createProfile(@Valid @RequestBody CreateProfileModel createModel) throws LoginAlreadyExistsException {
        profileService.createNewProfile(createModel);
    }

    @PostMapping("edit")
    public void editProfile(@Valid @RequestBody EditProfileDataModel editModel) throws ProfileNotFoundException {
        profileService.editProfile(editModel);
    }

    @PostMapping("/editCredentials")
    public void editCreds(@Valid @RequestBody EditCredentialsModel editModel) throws ProfileNotFoundException {
        profileService.editCredentials(editModel);
    }

    @GetMapping("my")
    public ProfileModel getPrincipalProfile(Authentication authToken) throws ProfileNotFoundException {
        return new ProfileModel(profileService.getProfile((UUID) authToken.getPrincipal()));
    }

    @GetMapping("all")
    public Page<ProfileModel> getAllProfiles(@RequestParam int page,
                                        @RequestParam int pageSize,
                                        @RequestParam(required = false) String sort){
        if (sort == null) sort = "login";
        return profileService.getAllProfiles(PageRequest.of(page, pageSize, Sort.by(sort)));
    }
}
