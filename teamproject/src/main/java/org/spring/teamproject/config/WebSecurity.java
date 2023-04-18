package org.spring.teamproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final UserDetailSecurity userDetailSecurity;
    private final AuthenticationFailureHandler customFailHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable(); //페이지 보안 설정 Exception 예외 처리 필수
        http.userDetailsService(userDetailSecurity);
        //권한설정
        http.authorizeHttpRequests()
                .antMatchers("/","/trackList","/login","/join","board/**").permitAll() //모든사용자 접근가능
                .antMatchers("/member/**").authenticated()                            //로그인시 접근가능
                .antMatchers("/member/**").hasAnyRole("MEMBER","ADMIN")         //MEMBER,ADMIN 권한만접근가능
                .antMatchers("/admin/**").hasRole("ADMIN");                           //ADMIN 권한만 접근가능
        //login&logout
        http.formLogin()
                .loginPage("/login")                                //로그인 요청시 security 로그인페이지
                .loginProcessingUrl("/login")                       //로그인 form에서 실행 POST
                .usernameParameter("email")                         //로그인시 아이디
                .passwordParameter("password")                      //로그인시 비밀번호
                .failureHandler(customFailHandler)                  //실패시 핸들러
//                ===================================================
                .defaultSuccessUrl("/memberMain")                             //로그인 성공시 url
//              .failureUrl("/")                          //로그인 실패시 url
//                ====================================================
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))     //logout 입력시 security 로그아웃
                .logoutSuccessUrl("/");                                                //로그아웃 성공시 url
        return http.build();
    }
    @Bean  // 비밀번호 암호화
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}