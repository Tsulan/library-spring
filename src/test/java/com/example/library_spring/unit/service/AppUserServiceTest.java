package com.example.library_spring.unit.service;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.repository.AppUserRepository;
import com.example.library_spring.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_Success() {
        AppUser user = new AppUser(null, "John Doe", "john@example.com", "password123");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);

        AppUser createdUser = appUserService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
    }

    @Test
    public void createUser_ThrowException_WhenEmailAlreadyExists() {
        AppUser user = new AppUser(null, "John Doe", "john@example.com", "password123");
        when(appUserRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> appUserService.createUser(user));
    }

    @Test
    public void updateUser_ThrowsException_WhenEmailAlreadyInUse() {
        AppUser existingUser = new AppUser(1L, "John Doe", "john@example.com", "password123");
        AppUser updatedUser = new AppUser(1L, "John Doe", "jane@example.com", "password123");
        AppUser anotherUser = new AppUser(2L, "Jane Doe", "jane@example.com", "password456");

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(appUserRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(anotherUser));

        assertThrows(IllegalArgumentException.class, () -> appUserService.updateUser(1L, updatedUser));
    }

//    TODO Add more tests for other methods (getUserById, updateUser, deleteUser)
}
