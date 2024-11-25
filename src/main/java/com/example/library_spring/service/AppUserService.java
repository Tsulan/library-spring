package com.example.library_spring.service;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public AppUser createUser(AppUser user) {
        if (appUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        return appUserRepository.save(user);
    }

    public AppUser updateUser(Long id, AppUser updatedUser) {
        AppUser existingUser = getUserById(id);

        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            appUserRepository.findByEmail(updatedUser.getEmail()).ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new IllegalArgumentException("Email is already in use by another user");
                }
            });
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());

        return appUserRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        appUserRepository.deleteById(id);
    }

    public AppUser registerUser(AppUser user) {
        if (appUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

}
