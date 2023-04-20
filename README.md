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
</details>
 ===================================================================================================================================================================
<details>
<summary>Login</summary><blockquote>
 
 <details><summary>Controller</summary><blockquote>
 
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
 </blockquote></details>
 <details><summary>Login Fail Handler</summary><blockquote>
  
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
  </blockquote></details>
 
 <details><summary>View</summary><blockquote>
  
 ### Login View
 ![image](https://user-images.githubusercontent.com/106312692/233266010-59991354-ab58-4050-9c8a-f0ef39a295ff.png)
 
 ### Login Fail
 ![image](https://user-images.githubusercontent.com/106312692/233266097-1b648539-9e44-4aed-83f4-edf345692f0e.png)
 
 </blockquote></details>
</blockquote></details>

  ===================================================================================================================================================================
<details><summary>Create</summary><blockquote>
 
 <details><summary>Controller</summary><blockquote>
  
  ### Get Controller
```
@GetMapping("/join")                                //회원가입페이지 이동
    public String join(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "/pages/member/join";
    }
```
 ### Post Controller
 ```
 @PostMapping("/join")                               //form 받아 회원가입실행
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
 ```
  ### 중복체크
  ```
  @PostMapping("/emailChecked")    //회원가입 email 중복체크버튼
    public @ResponseBody int nameChecked(
            @RequestParam String email) {
        int rs = memberService.findByUserNameDo(email);
        return rs;
    }
  ```
   </blockquote></details>

 <details><summary>Service</summary><blockquote>
 
  ```
  @Transactional  // 회원추가
    public void insertMember(MemberDto memberDto) {
        MemberEntity memberEntity= MemberEntity.memberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional  // admin추가
    public void insertAdmin(MemberDto memberDto) {
        MemberEntity memberEntity= MemberEntity.adminEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional  //회원가입 이메일 중복체크
    public int findByUserNameDo(String email) {
        Optional<MemberEntity> memberEntity =memberRepository.findByEmail(email);
        if(memberEntity.isPresent()){
            //이름이있으면(중복)
            return 0;
        }else {
            //이름이없으면(중복x)
            return 1;
        }
    }
  ```
 </blockquote></details>
  
  <details><summary>Dto</summary><blockquote>
   
   ```
   private Long no;
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]{3,4}$", message = "정확한 이름을 입력해주세요")
    private String userName;
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$",message = "정확한 핸드폰번호를입력하세요")
    private String phone;
    private String zip_code;
    private String homeAddress;
    private String DetailAddress;
    private Role role;
    private LocalDateTime createTime; //생성시에만 적용
    private LocalDateTime updateTime;// 수정 시에 적용
   ```
  </blockquote></details>
  
  
<details><summary>Entity</summary><blockquote>
 
  ### Entity
 ```
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long no;
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    private String zip_code;
    private String homeAddress;
    private String DetailAddress;
    private String userName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
 ```
  </blockquote></details>
  
  <details><summary>카카오 우편번호 api</summary><blockquote>
   
   <details><summary>Js</summary><blockquote>
    
```
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
                    function sample4_execDaumPostcode() {
                      new daum.Postcode({
                        oncomplete: function (data) {
                          var roadAddr = data.roadAddress; // 도로명 주소 변수
                          var extraRoadAddr = ''; // 참고 항목 변수
                          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                            extraRoadAddr += data.bname;
                          }
                          if (data.buildingName !== '' && data.apartment === 'Y') {
                            extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                          }
                          if (extraRoadAddr !== '') {
                            extraRoadAddr = ' (' + extraRoadAddr + ')';
                          }
                          document.getElementById('sample4_postcode').value = data.zonecode;
                          document.getElementById("sample4_roadAddress").value = roadAddr;
                          // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                          if (roadAddr !== '') {
                            document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                          } else {
                            document.getElementById("sample4_extraAddress").value = '';
                          }
                          var guideTextBox = document.getElementById("guide");
                          // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                          if (data.autoRoadAddress) {
                            var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                            guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                            guideTextBox.style.display = 'block';
                          } else if (data.autoJibunAddress) {
                            var expJibunAddr = data.autoJibunAddress;
                            guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                            guideTextBox.style.display = 'block';
                          } else {
                            guideTextBox.innerHTML = '';
                            guideTextBox.style.display = 'none';
                          }
                        }
                      }).open();
                    }
                  </script>
 ```
  </blockquote></details>

  </blockquote></details>
  
  <details><summary>View</summary><blockquote>
   <details><summary>html</summary><blockquote>
    
 ```
    <div class="container">
    <div class="join">
        <div class="join-form">
            <form th:action="@{/join}" method="post" th:object="${memberDto}">
                <ul>
                    <li class="s1">Sign Up</li>
                    <li class="s2"><input type="email" th:field="*{email}" name="email" id="email" placeholder="Enter Email" class="text" minlength="4" maxlength="30">
                        <span class="error" th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></span>
                        <input type="button" value="중복확인" id="emailChecked"></li>
                    <li class="s2"><input type="password" th:field="*{password}" name="password" id="password" placeholder="Enter Password" class="text" minlength="4" maxlength="10">
                        <span class="error" th:if="${#fields.hasErrors('password')}" th:errors="*{password}"></span></li>
                    <div class="adrress">
                        <input type="text" id="sample4_postcode" th:field="${memberDto.zip_code}" placeholder="우편번호">
                        <input type="button" onclick="sample4_execDaumPostcode()" value="우편번호 찾기"><br>
                        <input type="text" id="sample4_roadAddress" th:field="${memberDto.homeAddress}" placeholder="도로명주소" size="30">
                        <span id="guide" style="color:#999;display:none"></span>
                        <input type="text" id="sample4_detailAddress" th:field="${memberDto.DetailAddress}" placeholder="상세주소" size="30">
                    </div>
                    <li class="s2"><input type="text" th:field="*{userName}" id="yy" placeholder="Enter Name" class="int" minlength="2" maxlength="3">
                        <span class="error" th:if="${#fields.hasErrors('userName')}" th:errors="*{userName}"></span></li>
                    <li class="s2">
                        <input type="text" th:field="*{phone}" name="phone" id="phone" placeholder="Phone Number" class="s5" minlength="11" maxlength="11">
                        <span class="error" th:if="${#fields.hasErrors('phone')}" th:errors="*{phone}"></span>
                    </li>
                    <li class="s4"><input type="submit" class="joinOk" value="sign up"></li>
                </ul>
            </form>
        </div>
    </div>
</div>
```
</blockquote></details>
  
   #### create
   ![image](https://user-images.githubusercontent.com/106312692/233277145-d68a28c9-404d-4a2d-bf20-3c7882adca30.png)

   #### Fail
   ![image](https://user-images.githubusercontent.com/106312692/233277281-7edf20f8-cbb0-4405-b9d3-3504b40d47ed.png)

   #### 우편번호Api
   ![image](https://user-images.githubusercontent.com/106312692/233277438-02e57625-a4e6-4fe6-809f-be3d72e4abf8.png)

  </blockquote></details>
  
<blockquote></details>

   ===================================================================================================================================================================
  <details><summary>Read</summary><blockquote>
  
   <details><summary>Controller</summary><blockquote>
    
  ```
    @GetMapping("/mypage/{email}")                           //나의 정보 가져온 후 mypage 입장
    public String membermypage(@PathVariable String email, Model model) {
        MemberDto memberDto = memberService.memberDetail(email);
        model.addAttribute("member", memberDto);
        return "/pages/member/mypage";
    }
  ```
  </blockquote></details>
   
   <details><summary>Service</summary><blockquote>
  
  ```
    public MemberDto memberDetail(String email) {
        Optional<MemberEntity> memberEntity=memberRepository.findByEmail(email);
        if (!memberEntity.isPresent()){
            return null;
        }
        MemberDto memberDto=MemberDto.updateMemberDto(memberEntity.get());
        return memberDto;
    }
  ```
  </blockquote></details>

   <details><summary>View</summary><blockquote>
     
   <details><summary>Html</summary><blockquote>
      
   ```
      <div class="container">
    <div class="container-con">
        <h1 class="logo">Credentials</h1>
        <div class="accont-row">
            <div class="row-text"><label>E-mail address</label> <br>
                <input class="info-text" type="text" th:value="${member.email}" readonly>
            </div>
            <div class="row-text"><label class="address">Billing address</label> <br>
                <input class="info-text" type="text"  th:value="${member.zip_code}" readonly><br>
                <input class="info-text" type="text"  th:value="${member.homeAddress}" readonly><br>
                <input class="info-text" type="text"  th:value="${member.DetailAddress}" readonly>
            </div>
            <div class="row-text"><label>Username</label> <br>
                <input class="info-text" type="text"  th:value="${member.userName}" readonly>
            </div>
            <div class="row-text"><label>Phone-number</label> <br>
                <input class="info-text" type="text"  th:value="${member.phone}" readonly>
            </div>
            <div class="user-up">
                <button><a th:href="@{|/member/update/${#authentication.principal.username}|}">회원수정</a></button>
            </div>
        </div>
    </div>
</div>
 ```
  </blockquote></details>
    
  ![image](https://user-images.githubusercontent.com/106312692/233283584-83c0c647-3036-4f60-a277-6c12fb34da9f.png)
   </blockquote></details>

  </blockquote></details>

===================================================================================================================================================================
   
<details><summary>Update</summary><blockquote>
  
 
<details><summary>Controller</summary><blockquote>
  
 ```
 @GetMapping("/update/{email}")                           //나의 정보를 가져가서 회원수정페이지 입장
    public String info(@PathVariable String email, Model model) {
        MemberDto memberDto = memberService.memberDetail(email);
        model.addAttribute("member", memberDto);
        return "/pages/member/update";
    }
    @PostMapping("/update")                                  //회원수정 실행
    public String updatePost(@ModelAttribute MemberDto memberDto) {
        memberService.updateOk(memberDto);
        return "redirect:/";
 ```
  </blockquote></details>
 
 <details><summary>Service</summary><blockquote>
  
  ```
  @Transactional  //회원수정
    public void updateOk(MemberDto memberDto) {
        MemberEntity memberEntity=MemberEntity.updateMemberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
  ```
  </blockquote></details>
 
 
<details><summary>View</summary><blockquote>
  
 <details><summary>Html</summary><blockquote>
  
  ```
<div class="container">
    <div class="container-con">
        <h1 class="logo">Edit My Infomation</h1>
        <div class="accont-row">
            <form th:action="@{/member/update}" method="post" th:object="${memberDto}">
                <div class="row-text"><label>E-mail address</label> <br>
                    <input type="hidden" name="no" th:value="${member.no}">
                    <input class="info-text" type="email" name="email" minlength="4" maxlength="15" th:value="${member.email}" readonly>
                </div>
                <div class="row-text"><label>Password</label> <br>
                    <input class="info-text" type="password" name="password" minlength="4" maxlength="10" th:value="${member.password}">
                </div>
                <div class="row-text"><label class="address">Billing address</label> <br>
                        <input class="info-text" type="text" name="zip_code" th:value="${member.zip_code}" ><br>
                        <input class="info-text" type="text" name="homeAddress" th:value="${member.homeAddress}" ><br>
                        <input class="info-text" type="text" name="DetailAddress" th:value="${member.DetailAddress}" >
                </div>
                <div class="row-text"><label>Username</label> <br>
                    <input class="info-text" type="text" name="userName" minlength="2" maxlength="3" th:value="${member.userName}">
                </div>
                <div class="row-text"><label>Phone-number</label> <br>
                    <input class="info-text" type="text" name="phone" minlength="11" maxlength="11" th:value="${member.phone}">
                </div>
                <div class="user-up"><input type="submit" value="회원수정">
                    <button><a th:href="@{|/member/delete/${member.no}|}">회원탈퇴</a></button>
                </div>
            </form>
        </div>
    </div>
</div>
  ```
  </blockquote></details>

 ![image](https://user-images.githubusercontent.com/106312692/233286441-78348d04-2bf4-4b29-9e2c-dc36f73bf182.png)
  </blockquote></details>
  </blockquote></details>
   ===================================================================================================================================================================
   
<details><summary>Delete</summary><blockquote>
  
<details><summary>Controller</summary><blockquote>
  
 ```
 @GetMapping("/delete/{no}")                             //회원 탈퇴후 security logout
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
  </blockquote></details>
 
<details><summary>Service</summary><blockquote>
  
 ```
 @Transactional //회원삭제
    public int deleteOk(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).get();
        memberRepository.delete(memberEntity);
        if(memberRepository.findById(id)!=null){
            return 0;
        }
        return 1;
    }
 ```
  </blockquote></details>

<details><summary>Html</summary><blockquote>
 
 ```
 <button><a th:href="@{|/member/delete/${member.no}|}">회원탈퇴</a></button>
 ```
  </blockquote></details>


  </blockquote></details>

   
   
