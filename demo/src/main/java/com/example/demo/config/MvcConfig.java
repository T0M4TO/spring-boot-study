package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  // 요청 - 뷰 연결
  public void addViewControllers(ViewControllerRegistry registry) {
    //registry.addViewController("/").setViewName("board/list");
    //registry.addViewController("/login").setViewName("login");
    registry.addViewController("/admin").setViewName("admin");
    registry.addViewController("/signup").setViewName("signup");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .exposedHeaders("X-AUTH-TOKEN")
            .allowCredentials(true)
            .allowedOrigins("http://localhost:8080");
  }
}