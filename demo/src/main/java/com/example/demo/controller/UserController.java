package com.example.demo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.domain.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@Controller
public class UserController {

  private final UserService userService;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;

  @PostMapping(value = "/user")
  public String signup(UserInfoDto infoDto) { // 회원 추가
    userRepository.save(UserInfo.builder()
            .email(infoDto.getEmail())
            .password(passwordEncoder.encode(infoDto.getPassword()))
            .auth(infoDto.getAuth())
            .build());
    //userService.save(infoDto);
    return "redirect:login";
  }

  @GetMapping(value = "/login")
  public String login() {
    return "login";
  }

  @PostMapping(value = "/login")
  public String login(UserInfoDto user, HttpServletResponse response) {
    System.out.println("3333333 : ");
    System.out.println("3333333 : "+userRepository.findByEmail(user.getEmail()));
    if(userRepository.findByEmail(user.getEmail()) == null)
    {
      throw new IllegalArgumentException("가입되지 않은 E-MAIL 입니다.");
    }
    UserInfo member = userRepository.findByEmail(user.getEmail());

    if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("잘못된 비밀번호입니다.");
    }

    String token = jwtAuthenticationProvider.createToken(member.getCode(),member.getUsername(), member.getAuth());
    response.setHeader("X-AUTH-TOKEN", token);
    System.out.println(token);
    Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);

    return "board/list";
  }

  @GetMapping(value = "/logout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    return "redirect:login";
  }
}
