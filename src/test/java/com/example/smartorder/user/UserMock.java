package com.example.smartorder.user;

import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@NoArgsConstructor
public class UserMock {
    public final JoinUserCommand joinUserCmd = JoinUserCommand.builder()
            .accessId("mockAccessId")
            .password("testPassword!")
            .name("테스트")
            .ageGroup(AgeGroup.Thirty)
            .gender(Gender.Women)
            .tel("01012345678")
            .build();
    public final User user = User.createBy(
            this.joinUserCmd,
            PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(this.joinUserCmd.getPassword())
    );
}
