package com.example.demo.config;

import com.example.demo.util.JwtAuthenticationFilter;
import com.example.demo.util.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // Security 사용
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Override
  public void configure(WebSecurity web) { // static 하위 파일 목록(css, js, img) 인증 무시
    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/h2-console/**","/webjars/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception { // http 관련 인증 설정
    http
            .httpBasic().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);
  /*        .authorizeRequests() // 접근에 대한 인증 설정
          .antMatchers("/login", "/signup", "/user").permitAll() // 누구나 접근 허용
          .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
          .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
          .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
        .and()*/
            //.formLogin() // 로그인에 관한 설정
            //.loginPage("/login") // 로그인 페이지 링크
            //.defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
        //.and()
          //.logout() // 로그아웃
            //.logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
            //.invalidateHttpSession(true); // 세션 날리기

  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
