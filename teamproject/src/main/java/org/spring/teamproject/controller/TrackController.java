package org.spring.teamproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.ItemDto;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.service.CartService;
import org.spring.teamproject.service.ItemService;
import org.spring.teamproject.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TrackController {

    private final ItemService itemService;
    private final CartService cartService;
    private final MemberService memberService;
    // track 추가
    @GetMapping("/trackAdd")
    public String addView(Model model) {
        model.addAttribute("itemDto", new ItemDto());

        return "/pages/track/trackInsert";
    }

    // track 추가 실행
    @PostMapping("/trackAdd")
    public String addmet(@Valid ItemDto itemDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/pages/track/trackInsert";
        }
        itemService.itemInsert(itemDto);

        return "redirect:/trackList";
    }

    // track 목록
    @GetMapping("/trackList")
    public String itemList(Model model, @PageableDefault(page = 0,size = 10,sort = "no",
            direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(value = "search",required = false) String search) {

        // 검색어가 없으면 전체목록 페이징
        if(search==null){
            Page<ItemDto> list =itemService.itemPage(pageable);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            MemberDto memberDto= memberService.memberDetail(email);

            Long total=list.getTotalElements(); // 전체 레코드 수
            int bockNum=4;
            int nowPage= list.getNumber()+1 ;// 현재페이지-> boardList.getNumber()는 0부터 시작
            int startPage=Math.max(1,list.getNumber()-bockNum); //시작페이지 -> 기본이 최소 1페이지
            int endPage=list.getTotalPages(); // 마지막페이지

            if(memberDto!=null) {
                model.addAttribute("member", memberDto.getNo());
            }

            model.addAttribute("list",list);
            model.addAttribute("total",total);
            model.addAttribute("nowPage",nowPage);
            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
        // 검색어가 있으면 검색된 목록만 페이징
        }else{
            Page<ItemDto> list = itemService.search(search,pageable);

            Long total=list.getTotalElements(); // 전체 레코드 수
            int bockNum=4;
            int nowPage= list.getNumber()+1 ;// 현재페이지-> boardList.getNumber()는 0부터 시작
            int startPage=Math.max(1,list.getNumber()-bockNum); //시작페이지 -> 기본이 최소 1페이지
            int endPage=list.getTotalPages(); // 마지막페이지

            model.addAttribute("list",list);
            model.addAttribute("total",total);
            model.addAttribute("nowPage",nowPage);
            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
        }



        return "pages/track/trackList";
    }

    /*// track 상세목록
    @GetMapping("/trackDetail/{itemNo}/{memberNo}")
    public String trackDetail(@PathVariable("no") long no, @PathVariable("memberNo") Long memberNo, Model model){
         List<ItemDto> itemDto=itemService.trackDetailRandom(no);
        MemberDto memberDto = cartService.memberDtoSearch(memberNo);

        if(itemDto!= null){
            model.addAttribute("dto", itemDto);
            model.addAttribute("itemDto",itemDto);
            model.addAttribute("member",memberDto);
            return "pages/track/trackDetail";
        }else{
            return null;
        }*/

//    }

    @GetMapping("/trackDetail/{itemNo}/{memberNo}")
    public String trackDetail(@PathVariable("itemNo") Long itemNo,@PathVariable("memberNo") Long memberNo, Model model){

        ItemDto dto=itemService.trackDetail(itemNo);
        MemberDto memberDto = cartService.memberDtoSearch(memberNo);
        List<ItemDto> itemDto=itemService.trackDetailRandom(itemNo);
        if(dto!= null){
            model.addAttribute("dto",dto);
            model.addAttribute("member",memberDto);
            model.addAttribute("itemDto",itemDto);
        }

        return "pages/track/trackDetail";
    }

    // track 수정
    @GetMapping("/trackUpdate/{no}")
    public String trackUpdate(@PathVariable("no") long no, Model model){
        ItemDto dto=itemService.trackUpdate(no);

        model.addAttribute("dto",dto);

        return "pages/track/trackUpdate";
    }


    // track 수정실행
    @PostMapping("/trackUpdate")
    public String trackUpdateOk(@ModelAttribute ItemDto itemDto)  {


        itemService.trackUpdateOk(itemDto);

        return "redirect:/trackList";
    }

    // track 삭제
    @GetMapping("/trackDelete/{no}")
    public String trackDelete(@PathVariable("no") long no){

        itemService.trackDelete(no);


        return "redirect:/trackList";

    }

}
