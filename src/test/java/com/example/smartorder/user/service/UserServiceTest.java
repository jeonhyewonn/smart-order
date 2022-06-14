package com.example.smartorder.user.service;

import com.example.smartorder.user.UserMock;
import com.example.smartorder.user.domain.Account;
import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import com.example.smartorder.user.service.dto.LoginUserCommand;
import com.example.smartorder.user.service.dto.UpdateProfileCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
        User existingUser = User.createBy(user);
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
        Account existingAccount = existingUser.getAccount();
        LoginUserCommand user = LoginUserCommand.builder()
                .accessId(existingAccount.getAccessId())
                .password(existingAccount.getPassword())
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
        User existingUser = UserMock.user;
        UUID userId = existingUser.getId();
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
                .ageGroup(AgeGroup.Forty)
                .gender(Gender.Women)
                .tel("010-4321-8765")
                .build();
        when(this.userRepository.findById(userId)).thenReturn(existingUser);

        // When
        this.userService.updateProfile(userId, profile);

        // Then
        User updatedUser = this.userRepository.findById(userId);
        assertThat(updatedUser.getName()).isEqualTo(profile.getName());
        assertThat(updatedUser.getAgeGroup()).isEqualTo(profile.getAgeGroup());
        assertThat(updatedUser.getGender()).isEqualTo(profile.getGender());
        assertThat(updatedUser.getTel()).isEqualTo(profile.getTel());
    }

    // 탈퇴
    // 존재하지 않는 사용자면 에러
    // 존재하는 사용자면 탈퇴
    @Test
    public void deleteAccountWithUnknownUserWillFail() {

    }

    @Test
    public void deleteAccountWillSucceed() {

    }
}