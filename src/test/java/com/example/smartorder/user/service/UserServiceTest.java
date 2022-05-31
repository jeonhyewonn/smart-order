package com.example.smartorder.user.service;

import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.repository.UserRepository;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    // join
    // 존재하는 사용자면 에러
    // 존재하지 않는 사용자면 넘어감 - id 리턴
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
        fail("Test has failed");
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
        User newUser = User.createBy(user);

        doReturn(null).when(this.userRepository).findByAccessId(accessId);

        // When
        UUID userId = this.userService.join(user);

        // Then
        assertThat(userId).isNotEqualTo(null);
    }

    // login
    // 존재하지 않는 사용자면 에러
    // 존재하는 사용자면 id 리턴
    @Test
    public void loginWithUnknownUserWillFail() {

    }

    @Test
    public void loginWillSucceed() {

    }

    // 프로필 조회
    // 존재하지 않는 사용자면 에러
    // 존재하는 사용자면 user entity 리턴
    @Test
    public void getProfileWithUnknownUserWillFail() {

    }

    @Test
    public void getProfileWillSucceed() {

    }

    // 프로필 수정
    // 존재하지 않는 사용자면 에러
    // 존재하는 사용자면 데이터 수정
    @Test
    public void updateProfileWithUnknownUserWillFail() {

    }

    @Test
    public void updateProfileWillSucceed() {

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