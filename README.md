# 1차 Shopping Mall Project
 
##  개요
- SpringFramework, Chat-bot을 이용한 쇼핑몰 웹 프로젝트


##  프로젝트 기간
   2023. 02-13 ~ 2023. 03-13

##  참여 인원
- 팀장 : 이지창 - DB설계,장바구니 서비스(CRD) ,마이페이지 , AWS EC2 배포
- 팀원1 : 강창신 - 페이지 Header&Footer layout , main&Admin 페이지 , 게시판 댓글 기능
- 팀원2 : 김득주 - 회원(CRUD) , Spring Security ,로그인
- 팀원3 : 장효선 - 상품서비스(CRUD) , 전체적인 디자인 수정 , Chat-bot 
- 팀원4 : 허인경 - 게시판서비스(CRUD), PPT제작

## 개발환경
![image](https://user-images.githubusercontent.com/106312692/233262427-fdc7c793-0b40-4ae0-ba83-0066d97e8472.png)

## 프로젝트 소개
![image](https://user-images.githubusercontent.com/106312692/233262665-88aacd9f-825c-4203-a2ba-13540fe2f408.png)

## 선정이유

### 홈페이지의 특징
- 여타 쇼핑몰과 달리 항목이 많지 않고 상품 이미지가 난잡하지 않아 차분한 느낌
- 여러 상품(음악 트랙) 목록이 한 눈에 들어오고, 하나의 트랙을 조회했을 때 관련 있는 다른 트랙도 확인할 수 있는 편의성

<hr>

## Code

<details>
<summary>Security</summary>
 
 ### WebSecurity
 
```
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
 
```
### UserDetailSecurity

 ```
 //아이디 체크 -> 인증과정
    @Override                               //로그인할 id
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //id 정해지면 레파지토리에서 쿼리메소드 생성(findBy??)
        Optional<MemberEntity> memberEmail = memberRepository.findByEmail(email);
        if (!memberEmail.isPresent()) {
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }
        MemberEntity memberEntity = memberEmail.get();              // 사용자가 있으면 get
        //인증된 회원의 인가(권한 설정)
        return User.builder()    //스프링관리자 User 역할을 빌더로 간단하게만듬
                .username(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .roles(memberEntity.getRole().toString())
                .build();
    }
}
```
 ### CustomAuthFailureHandler
 
 ```
 @Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException){
            errorMessage ="아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        }else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수없습니다 관리자에게 문의해주세요.";
        }else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        }else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        }else{
            errorMessage="알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요";
        }
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=true&exception="+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
 ```
</details>
 
<details>
<summary>Login</summary>
 
 ### Login Controller
```
@GetMapping("/login")                               //로그인
    public String login(@RequestParam(value = "error" ,required = false ) String error,
                        @RequestParam(value = "exception" ,required = false)String exception,
                        Model model) {
    model.addAttribute("error",error);
    model.addAttribute("exception",exception);
        return "/pages/member/login";
    }
```

 ![image](https://user-images.githubusercontent.com/106312692/233266010-59991354-ab58-4050-9c8a-f0ef39a295ff.png)
 ![image](https://user-images.githubusercontent.com/106312692/233266097-1b648539-9e44-4aed-83f4-edf345692f0e.png)
 
</details>



