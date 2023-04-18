package org.spring.teamproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.CartItemDto;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.service.CartService;
import org.spring.teamproject.service.ItemService;
import org.spring.teamproject.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.lang.reflect.Member;
import java.rmi.MarshalledObject;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final CartService cartService;





    @GetMapping("/cart")
    public String cartView( Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        MemberDto memberDto = memberService.memberDetail(email);


        List<CartItemDto> cartItemDtos= cartService.cartVIew();

        model.addAttribute("cartItem",cartItemDtos);
        model.addAttribute("member",memberDto.getUserName());
        return "pages/member/myitem";
    }



    //장바구니 담기
    @PostMapping("/cart/{memberNo}/{itemNo}")
    public String cartAdd(@PathVariable("memberNo") Long memberNo, @PathVariable("itemNo") Long itemNo, Model model){

        MemberEntity memberEntity= cartService.memberSearch(memberNo);
        ItemEntity itemEntity = cartService.itemSearch(itemNo);


        cartService.cartAdd(memberEntity,itemEntity);

        return "redirect:/cart";
    }






}
