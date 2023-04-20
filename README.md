# 1suck2jo Shopping Mall TeamProject


## Table <!-- omit in toc -->
<details>

- [AuthenticationFailureHandler](#AuthenticationFailureHandler)
- [UserDetailSecurity](#UserDetailSecurity)
- [WebSecurity](#WebSecurity)
- [MainController](#MainController)
- [MemberController](#MemberController)
- [Dto](#Dto)
- [Entity](#Entity)
- [Repository](#Repository)
- [MemberService]
  - [MemberService1](#MemberService1)
  - [MemberService2](#MemberService2)
- [Header](#Header)

## AuthenticationFailureHandler
![AuthenticationFailureHandler](https://user-images.githubusercontent.com/106312692/225341552-51658a47-9dee-49fc-8372-82bd4d12f597.PNG)
```
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
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
```
**SimpleUrlAuthenticationFailureHandler** 상속받아 **AuthenticationException** 이용하여 로그인 실패 사유마다 메시지설정

## UserDetailSecurity
![UserDetailSecurity](https://user-images.githubusercontent.com/106312692/223304847-f5a0da4e-b92d-4ce5-90fb-f76179401d05.PNG)
```UserDetailSecurity
    @Override                           
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<MemberEntity> memberEmail = memberRepository.findByEmail(email);

        if (!memberEmail.isPresent()) {
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }
        MemberEntity memberEntity = memberEmail.get(); 
        return User.builder()
                .username(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .roles(memberEntity.getRole().toString())
                .build();
    }
}
```
**Spring Security**에서 사용자의 정보를 담는 **UserDetails** 인터페이스



## WebSecurity
![WebSecurity2](https://user-images.githubusercontent.com/106312692/225342009-096cba60-6365-416c-be82-691083342f3f.PNG)
```WebSecurity
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

**WebSecurity**클레스를 생성하고 **Springboot Security**를 이용하여 

권한설정을 통한 페이지 접근,로그인, 로그아웃,비밀번호 암호화 설정

## MainController
![MainController2](https://user-images.githubusercontent.com/106312692/225341755-b1306167-c601-4e30-8913-9625ad8b2add.PNG)
```MainController
@GetMapping({"/", "", "/index"})
    public String index() {
        return "/pages/main";
    }
@GetMapping("/join")                                //C 페이지 이동
    public String join(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "/pages/member/join";
    }
    @PostMapping("/join")                               // C
    public String joinPost(@Valid MemberDto memberDto,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "/pages/member/join";
        }
//        Admin 입력하기
        if(memberDto.getEmail().equals("admin@gmail.com")){
            memberService.insertAdmin(memberDto);
            return "/pages/admin/adminindex";
        }
        memberService.insertMember(memberDto);
        System.out.println("회원가입 성공");
        return "redirect:/login";
    }
    @PostMapping("/emailChecked")                        //C email 중복체크
    public @ResponseBody int nameChecked(
            @RequestParam String email) {
        int rs = memberService.findByUserNameDo(email);
        return rs;
    }
    @GetMapping("/login")                               //로그인 AuthenticationException
    public String login(@RequestParam(value = "error" ,required = false ) String error,
                        @RequestParam(value = "exception" ,required = false)String exception,
                        Model model) {
    model.addAttribute("error",error);
    model.addAttribute("exception",exception);

        return "/pages/member/login";
    }
```
권한 없이 이용할 수 있는 **MainController**

기본페이지 이동, 회원가입페이지이동, 회원가입이 성공하면 로그인페이지로이동설정

회원가입시 DB에 있는 email과 중복인지아닌지 판별기능, Security로그인페이지 이동등

## MemberController
![MemberController2](https://user-images.githubusercontent.com/106312692/225341903-a360affb-cdca-4c64-8682-57f8d38e5f50.PNG)
```MemberController
@GetMapping("/mypage/{email}")                           //R
    public String membermypage(@PathVariable String email, Model model) {
        MemberDto memberDto = memberService.memberDetail(email);
        model.addAttribute("member", memberDto);
        return "/pages/member/mypage";
    }
    @GetMapping("/update/{email}")                           //RU
    public String info(@PathVariable String email, Model model) {
        MemberDto memberDto = memberService.memberDetail(email);
        model.addAttribute("member", memberDto);
        return "/pages/member/update";
    }
    @PostMapping("/update")                                  //U
    public String updatePost(@ModelAttribute MemberDto memberDto) {
        memberService.updateOk(memberDto);
        return "redirect:/";
    }
    @GetMapping("/delete/{no}")                             //D
    public String delete(@PathVariable Long no) {
        int rs = memberService.deleteOk(no);
        if (rs == 1) {
            System.out.println("회원탈퇴 실패");
            return null;
        }
        System.out.println("회원탈퇴 성공");
        return "redirect:/logout";
    }
```

**member** 권한을 부여받은 계정들만 이용할수 있는 **MemberContorller**

조회,수정,삭제


## Dto
![Dto](https://user-images.githubusercontent.com/106312692/223305358-da4293ad-50ce-4a9b-a715-c2e8462457bb.PNG)
```Dto
public class MemberDto {
    private Long no;
    @NotBlank(message = "이메일을 입력 해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력 해주세요. 4글자이상 10글자 이하로 입력하세요")
    private String password;
    @NotBlank(message = "배송주소를 입력 해주세요")
    private String address;
    @NotBlank(message = "이름을 입력해주세요")
    private String userName;
    @NotBlank(message = "핸드폰 번호를 입력해주세요")
    private String phone;
    private Role role;
    private LocalDateTime createTime; 
    private LocalDateTime updateTime;
    public MemberDto(MemberEntity memberEntity) {
    }
```

계층간 데이터 교환을 위해 사용하는 객체 **DTO** 

회원 가입시 input 객체들이 유효하지않으면 에러메세지가 발생

## Entity
![Entity](https://user-images.githubusercontent.com/106312692/223305413-ff735ad6-9b1b-4aad-a2de-37425f4a8f81.PNG)
```Entity
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long no;
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    private String address;
    private String userName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
```

**DB Entity 테이블 입니다**


## Repository
![Repository](https://user-images.githubusercontent.com/106312692/223305532-3e0f1c68-ef9c-481d-934a-9b94cb307dfd.PNG)
```Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    Optional<MemberEntity> findByEmail(String email);
```

**JpaRepository** 인터페이스를 상속하여 메서드를 선언하지 않고 다양한 기능을 외부로 개방
**findById** 메소드를 사용해도 되지만 **findByEmail** 로 커스텀해보았습니다


## Role
![Role](https://user-images.githubusercontent.com/106312692/223305585-d2227d66-191f-4582-856f-a5bb58c7f238.PNG)
```Role
public enum Role {
    ADMIN,MEMBER
```

프로젝트 권한은 **MEMBER** ,**ADMIN** 이 두가지로만 하기로 협의하여 생성하였습니다

## MemberService1
![MemberService1](https://user-images.githubusercontent.com/106312692/223305644-4e26bd58-c4e0-455c-87a8-54dc9afa44af.PNG)
```MemberService1
 @Transactional  
    public void insertMember(MemberDto memberDto) {
        MemberEntity memberEntity= MemberEntity.memberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional      
    public int findByUserNameDo(String email) {
        Optional<MemberEntity> memberEntity =memberRepository.findByEmail(email);
        if(memberEntity.isPresent()){
            return 0;
        }else {
            return 1;
        }
    }
```

회원가입시 DB에 비밀번호가 암호화되게 설정
Optional isPresent 메소드를 이용하여 Entity 중복여부 확인

## MemberService2
![MemberService2](https://user-images.githubusercontent.com/106312692/223305690-a90f16a1-1e17-4a00-a79e-fda7b11c7c2e.PNG)
```MemberService2
public MemberDto memberDetail(String email) {
        Optional<MemberEntity> memberEntity=memberRepository.findByEmail(email);
        if (!memberEntity.isPresent()){
            return null;
        }
        MemberDto memberDto=MemberDto.updateMemberDto(memberEntity.get());
        return memberDto;
    }
    @Transactional 
    public void updateOk(MemberDto memberDto) {
        MemberEntity memberEntity=MemberEntity.updateMemberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional
    public int deleteOk(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).get();
        memberRepository.delete(memberEntity);
        if(memberRepository.findById(id)!=null){
            return 0;
        }
        return 1;
    }
}
```

회원조회, 회원수정, 회원삭제 Service

## Header
![Header](https://user-images.githubusercontent.com/106312692/223305722-434685d5-005a-48d8-8d1f-d408b638ce70.PNG)
```Header
                    <li class="li-last" sec:authorize="isAnonymous()">
                        <a th:href="@{/login}"></a>
                    </li>
                    
                    <li class="li-last" sec:authorize="isAuthenticated()">
                        <a th:href="@{|/member/mypage/${#authentication.principal.username}|}"></a>
                    </li>
```

비로그인시에 a태그 로그인 페이지로 이동

**Security** 로그인완료시 a태그  findByEmail정보를 불러와 **mypage**로 이동

