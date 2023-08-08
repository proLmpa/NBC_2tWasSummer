package com.example.itwassummer.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
            , message = "숫자/소문자/특수문자를 각각 1자씩 포함한 8자 이상으로 비밀번호를 구성해야 합니다.")
    private String password;

    private boolean admin;
    private String adminToken;
}
