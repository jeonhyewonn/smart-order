package com.example.smartorder.user.service;

import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import com.example.smartorder.user.service.dto.LoginUserCommand;
import com.example.smartorder.user.service.dto.UpdateProfileCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UUID join(JoinUserCommand user) {
        User existingUser = this.userRepository.findByAccessId(user.getAccessId());
        if (existingUser != null) throw new IllegalStateException("ExistingUser");

        User newUser = User.createBy(user, this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(newUser);

        return newUser.getId();
    }

    public UUID login(LoginUserCommand user) {
        User existingUser = this.userRepository.findByAccessId(user.getAccessId());
        if (existingUser == null) throw new IllegalStateException("UnknownUser");

        return existingUser.getId();
    }

    public User getUser(UUID id) {
        User user = this.userRepository.findById(id);
        if (user == null) throw new IllegalStateException("UnknownUser");

        return user;
    }

    @Transactional
    public void updateProfile(UUID id, UpdateProfileCommand profile) {
        User user = this.userRepository.findById(id);
        if (user == null) throw new IllegalStateException("UnknownUser");

        user.updateProfile(profile);
    }

    @Transactional
    public void changePassword(UUID id, String oriPassword, String newPassword) {
        User user = this.userRepository.findById(id);
        if (user == null) throw new IllegalStateException("UnknownUser");

        if (!this.passwordEncoder.matches(oriPassword, user.getPassword())) throw new IllegalStateException("IncorrectPassword");

        user.changePassword(this.passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void deactivateAccount(UUID id) {
        User user = this.userRepository.findById(id);
        if (user == null) throw new IllegalStateException("UnknownUser");

        user.deactivate();
    }
}
