package com.example.smartorder.user.service;

import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import com.example.smartorder.user.service.dto.LoginUserCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public void userService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UUID join(JoinUserCommand user) {
        User existingUser = this.userRepository.findByAccessId(user.getAccessId());
        if (existingUser != null) throw new IllegalStateException("ExistingUser");

        User newUser = User.createBy(user);
        this.userRepository.save(newUser);

        return newUser.getId();
    }
}
