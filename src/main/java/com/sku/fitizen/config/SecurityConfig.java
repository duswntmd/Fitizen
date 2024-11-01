package com.sku.fitizen.config;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Autowired
   DataSource dataSource;

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public WebSecurityCustomizer webSecurityCustomizer() {
      return (web) -> web.ignoring().requestMatchers("/resources/**", "/ignore2");
   }

   @Autowired
   public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
      log.info("데이터소스 설정");
      auth.jdbcAuthentication().dataSource(dataSource);

       /* users, authorities 이외의 다른 테이블 이름을 사용하는 경우에는 아래의 설정이 필수
       auth.jdbcAuthentication().dataSource(dataSource)
       .usersByUsernameQuery(
                "SELECT username,password, enabled FROM users WHERE username=?")
       .authoritiesByUsernameQuery(
                "SELECT username, authority FROM authorities WHERE username=?");
       */
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      log.info("접근제한 설정");
      http.authorizeHttpRequests(authz -> authz
              .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
              .requestMatchers("/", "/login/**", "/register/add", "/board/list", "/comments/list", "/user/myPage",
                                       "/findME", "findResult", "/exerciseDetail/**",
                                       "/css/**", "/Assets/**", "/boardimages/**", "/files/**", "/image/**", "/js/**"

              ).permitAll()
              .requestMatchers("/register/updateuser").hasAnyRole("USER")
              .requestMatchers("/register/deleteuser").hasAnyRole("USER")
              .requestMatchers("/board/write").hasAnyRole("USER")
              .requestMatchers("/board/search").hasAnyRole("USER")
              .requestMatchers("/board/search").hasAnyRole("USER")
              .requestMatchers("/board/view/**").hasAnyRole("USER")
              .requestMatchers("/board/edit/**").hasAnyRole("USER")
              .requestMatchers("/board/delete/**").hasAnyRole("USER")
              .requestMatchers("/board/download/**").hasAnyRole("USER")
              .requestMatchers("/board/like/**").hasAnyRole("USER")
              .requestMatchers("/board/unlike/**").hasAnyRole("USER")
              .requestMatchers("/comments/add/**").hasAnyRole("USER")
              .requestMatchers("/comments/edit/**").hasAnyRole("USER")
              .requestMatchers("/comments/delete/**").hasAnyRole("USER")
              .requestMatchers("/kakao/map/**").hasAnyRole("USER")
              .requestMatchers("/kakao/reviewDetail/**").hasAnyRole("USER")
              .requestMatchers("/kakao/addReview/**").hasAnyRole("USER")
              .requestMatchers("/kakao/editReview/**").hasAnyRole("USER")
              .requestMatchers("/kakao/deleteReview").hasAnyRole("USER")
                      //.anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
                     .anyRequest().denyAll()
      ).csrf( csrfConf -> csrfConf.disable()
      ).formLogin(loginConf -> loginConf.loginPage("/login/login")   // 컨트롤러 메소드와 지정된 위치에 로그인 폼이 준비되어야 함
              .loginProcessingUrl("/dologin")            // 컨트롤러 메소드 불필요, 폼 action과 일치해야 함
                .failureUrl("/login/login")      // 로그인 실패시 이동 경로(컨트롤러 메소드 필요함)
              //.failureForwardUrl("/login?error=Y")  //실패시 다른 곳으로 forward
              .defaultSuccessUrl("/", true)
              .usernameParameter("id")  // 로그인 폼에서 이용자 ID 필드 이름, 디폴트는 username
              .passwordParameter("pwd")  // 로그인 폼에서 이용자 암호 필트 이름, 디폴트는 password
              .permitAll()
      ).logout(logoutConf -> logoutConf.logoutRequestMatcher(new AntPathRequestMatcher("/login/logout")) //로그아웃 요청시 URL
              .logoutSuccessUrl("/login/login")   // 로그아웃 성공시 다시 로그인폼으로 이동
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .permitAll()

      ).exceptionHandling(exConf -> exConf.accessDeniedPage("/noaccess")); // 권한이 없어 접속 거부할 때

      return http.build();
   }
}
