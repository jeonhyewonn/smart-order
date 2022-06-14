package com.example.smartorder.user.service;

import com.example.smartorder.user.UserMock;
import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import com.example.smartorder.user.service.dto.LoginUserCommand;
import com.example.smartorder.user.service.dto.UpdateProfileCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserServiceTest() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.userService = new UserService(this.userRepository, this.passwordEncoder);
    }

    @Test
    public void joinWithExistingUserWillFail() {
        // Given
        JoinUserCommand user = JoinUserCommand.builder()
                .accessId("existingId")
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.Thirty)
                .gender(Gender.Women)
                .tel("01012345678")
                .build();
        User existingUser = User.createBy(user, this.passwordEncoder.encode(user.getPassword()));
        when(this.userRepository.findByAccessId(user.getAccessId())).thenReturn(existingUser);

        // When
        try {
            this.userService.join(user);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void joinWillSucceed() {
        // Given
        String accessId = "newId";
        JoinUserCommand user = JoinUserCommand.builder()
                .accessId(accessId)
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.Thirty)
                .gender(Gender.Women)
                .tel("01012345678")
                .build();
        when(this.userRepository.findByAccessId(user.getAccessId())).thenReturn(null);

        // When
        UUID userId = this.userService.join(user);

        // Then
        assertThat(userId).isNotEqualTo(null);
    }

    @Test
    public void loginWithUnknownUserWillFail() {
        // Given
        LoginUserCommand user = LoginUserCommand.builder()
                .accessId("unknownAccessId")
                .password("unknownPassword!")
                .build();
        when(this.userRepository.findByAccessId(user.getAccessId())).thenReturn(null);

        // When
        try {
            this.userService.login(user);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void loginWillSucceed() {
        // Given
        User existingUser = UserMock.user;
        LoginUserCommand user = LoginUserCommand.builder()
                .accessId(existingUser.getAccessId())
                .password(existingUser.getPassword())
                .build();
        when(this.userRepository.findByAccessId(user.getAccessId())).thenReturn(existingUser);

        // When
        UUID userId = this.userService.login(user);

        // Then
        assertThat(userId).isNotEqualTo(null);
    }

    @Test
    public void getUserWithUnknownUserWillFail() {
        // Given
        UUID userId = UUID.randomUUID();
        when(this.userRepository.findById(userId)).thenReturn(null);

        // When
        try {
            this.userService.getUser(userId);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void getUserWillSucceed() {
        // Given
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        User user = this.userService.getUser(userId);

        // Then
        assertThat(user).isEqualTo(existingUser);
    }

    @Test
    public void updateProfileWithUnknownUserWillFail() {
        // Given
        UUID userId = UUID.randomUUID();
        UpdateProfileCommand profile = UpdateProfileCommand
                .builder()
                .name("newName")
                .ageGroup(AgeGroup.Forty)
                .gender(Gender.Women)
                .tel("010-4321-8765")
                .build();
        when(this.userRepository.findById(userId)).thenReturn(null);

        // When
        try {
            this.userService.updateProfile(userId, profile);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void updateProfileWillSucceed() {
        // Given
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
        UpdateProfileCommand profile = UpdateProfileCommand
                .builder()
                .name("newName")
                .tel("010-4321-8765")
                .build();
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        this.userService.updateProfile(userId, profile);

        // Then
        User updatedUser = this.userRepository.findById(userId);
        assertThat(updatedUser.getName()).isEqualTo(profile.getName());
        assertThat(updatedUser.getTel()).isEqualTo(profile.getTel());
    }

    @Test
    public void changePasswordWithUnknownUserWillFail() {
        // Given
        UUID userId = UUID.randomUUID();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.userRepository.findById(userId)).thenReturn(null);

        // When
        try {
            this.userService.changePassword(userId, oriPassword, newPassword);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void changePasswordWithIncorrectOldPasswordWillFail() {
        // Given
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        try {
            this.userService.changePassword(userId, oriPassword, newPassword);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void changePasswordWillSucceed() {
        // Given
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
        String oriPassword = UserMock.joinUserCmd.getPassword();
        String newPassword = "newPassword";
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        this.userService.changePassword(userId, oriPassword, newPassword);

        // Then
        User updatedUser = this.userRepository.findById(userId);
        assertThat(this.passwordEncoder.matches(newPassword, updatedUser.getPassword())).isEqualTo(true);
    }

    @Test
    public void deactivateAccountWithUnknownUserWillFail() {
        // Given
        UUID userId = UUID.randomUUID();
        when(this.userRepository.findById(userId)).thenReturn(null);

        // When
        try {
            this.userService.deactivateAccount(userId);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void deactivateAccountWillSucceed() {
        // Given
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        this.userService.deactivateAccount(userId);

        // Then
        User deactivatedUser = this.userRepository.findById(userId);
        assertThat(deactivatedUser.getIsDeleted()).isEqualTo(true);
    }
}