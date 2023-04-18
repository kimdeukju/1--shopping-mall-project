package org.spring.teamproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/mypage/{email}")                           //나의 정보 가져온 후 mypage 입장
    public String membermypage(@PathVariable String email, Model model) {
        MemberDto memberDto = memberService.memberDetail(email);
        model.addAttribute("member", memberDto);
        return "/pages/member/mypage";
    }
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
    }
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

}
