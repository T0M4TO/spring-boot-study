package com.example.demo.dto;

import com.example.demo.domain.BoardInfo;
import com.example.demo.domain.FileInfo;
import com.example.demo.domain.UserInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class UserInfoDto {
  private Long code;
  private String email;
  private String password;
  private String auth;

  public UserInfo toEntity() {
    UserInfo build = UserInfo.builder()
            .code(code)
            .email(email)
            .password(password)
            .auth(auth)
            .build();
    return build;
  }

  @Builder
  public UserInfoDto(Long code, String email, String password, String auth) {
    this.code = code;
    this.email = email;
    this.password = password;
    this.auth = auth;
  }
}
