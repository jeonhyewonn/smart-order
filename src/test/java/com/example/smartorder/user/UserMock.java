package com.example.smartorder.user;

import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class UserMock {
    public static JoinUserCommand joinUserCmd = JoinUserCommand.builder()
            .accessId("mockAccessId")
            .password("testPassword!")
            .name("테스트")
            .ageGroup(AgeGroup.Thirty)
            .gender(Gender.Women)
            .tel("01012345678")
            .build();
    public static User user = User.createBy(
            joinUserCmd,
            PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(joinUserCmd.getPassword())
    );
}
