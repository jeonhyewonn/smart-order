package com.example.smartorder.user;

import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
import com.example.smartorder.user.domain.User;
import com.example.smartorder.user.service.dto.JoinUserCommand;

public class UserMock {
    public static User user = User.createBy(
            JoinUserCommand.builder()
                    .accessId("mockAccessId")
                    .password("testPassword!")
                    .name("테스트")
                    .ageGroup(AgeGroup.Thirty)
                    .gender(Gender.Women)
                    .tel("01012345678")
                    .build()
    );
}
