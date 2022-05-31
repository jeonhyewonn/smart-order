package com.example.smartorder.user.service;

import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UUID join(JoinUserCommand newUser) {
        User existingUser = this.userRepository.findByAccessId(newUser.getAccessId());
        if (existingUser != null) throw new IllegalStateException("ExistingUser");

        User user = User.createBy(newUser);
        this.userRepository.save(user);

        return user.getId();
    }
}
