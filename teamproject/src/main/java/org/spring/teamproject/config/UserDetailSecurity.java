package org.spring.teamproject.config;


import lombok.RequiredArgsConstructor;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailSecurity implements UserDetailsService {

    // 회원정보가 있는지 검사
    private final MemberRepository memberRepository;

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
