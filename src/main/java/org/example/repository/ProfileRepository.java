package org.example.repository;

import org.example.entity.Profile;
import org.example.entity.model.ProfileModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByLogin(String login);
    Optional<Profile> findById(UUID id);
    @Query("select p from Profile p")
    Page<ProfileModel> findAllModels(Pageable pageable);

    boolean existsByLogin(String login);
}
