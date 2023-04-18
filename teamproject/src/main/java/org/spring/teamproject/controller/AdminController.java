package org.spring.teamproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.ItemDto;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;

    @GetMapping({"/",""})                    //어드민 기본 페이지 설정
    public String adminpage() {
        return "/pages/admin/adminindex";
    }

    @GetMapping("/memberList")
    public String memberList(Model model){
        List<MemberDto> memberList=memberService.memberListDo();
        model.addAttribute("memberList",memberList);
        return "/pages/member/memberList";
    }
}
