package org.spring.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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


    @Transactional              //회원가입 이메일 중복체크
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
    //회원조회
    public MemberDto memberDetail(String email) {
        Optional<MemberEntity> memberEntity=memberRepository.findByEmail(email);
        if (!memberEntity.isPresent()){
            return null;
        }
        MemberDto memberDto=MemberDto.updateMemberDto(memberEntity.get());
        return memberDto;
    }
    @Transactional  //회원수정
    public void updateOk(MemberDto memberDto) {
        MemberEntity memberEntity=MemberEntity.updateMemberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional //회원삭제
    public int deleteOk(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).get();
        memberRepository.delete(memberEntity);
        if(memberRepository.findById(id)!=null){
            return 0;
        }
        return 1;
    }
    public List<MemberDto> memberListDo() {   //회원리스트
        List<MemberDto> memberDtoList=new ArrayList<>();
        List<MemberEntity> memberEntityList=memberRepository.findAll();

        for (MemberEntity memberEntity:memberEntityList){
            memberDtoList.add(MemberDto.adminUpdateDto(memberEntity));
        }
        return memberDtoList;
    }



}
