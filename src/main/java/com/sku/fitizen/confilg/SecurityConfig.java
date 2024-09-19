package com.sku.fitizen.confilg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        DataSource dataSource;    // JDBC Authentication에 필요함

            @Bean
            public UserDetailsService userDetailsService() {
                // 예시용 인메모리 사용자 생성
                UserDetails user1 = User.withDefaultPasswordEncoder()
                        .username("User1111")
                        .password("User1111")
                        .roles("USER")
                        .build();

                UserDetails user2 = User.withDefaultPasswordEncoder()
                        .username("User3333")
                        .password("User3333")
                        .roles("USER")
                        .build();

                UserDetails admin = User.withDefaultPasswordEncoder()
                        .username("Admin2222")
                        .password("Admin2222")
                        .roles("ADMIN")
                        .build();

                return new InMemoryUserDetailsManager(user1, user2, admin);
            }

            @Bean
            public PasswordEncoder passwordEncoder() {
                BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
                log.info("User1111->" + enc.encode("User1111")); // ROLE_USER
                log.info("User3333->" + enc.encode("User3333")); // ROLE_USER
                log.info("Admin2222->" + enc.encode("Admin2222")); // ROLE_ADMIN
                return enc;
            }

    //Enable jdbc authentication
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        log.info("데이터소스 설정");
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/ignore2");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Access restriction settings");

        http

                .formLogin((form) -> form
                        .loginPage("/login/login")
                        .loginProcessingUrl("/login/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login/login?error")
                        .permitAll()  // 로그인 페이지 접근 허용
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")  // 로그아웃 후 리디렉션 경로
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .exceptionHandling((ex) -> ex.accessDeniedPage("/denied"));

        return http.build();
    }

}