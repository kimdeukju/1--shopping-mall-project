package org.spring.teamproject.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.spring.teamproject.dto.CartDto;
import org.spring.teamproject.dto.CartItemDto;
import org.spring.teamproject.dto.ItemDto;

import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.entity.CartEntity;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.repository.CartItemRepository;
import org.spring.teamproject.repository.CartRepository;
import org.spring.teamproject.repository.ItemRepository;
import org.spring.teamproject.repository.MemberRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    //장바구니 담기
    public void cartAdd(MemberEntity memberEntity, ItemEntity itemEntity) {


        Optional<MemberEntity> memberid = memberRepository.findByNo(memberEntity.getNo());
        MemberEntity member = memberid.get();

        CartEntity cart = CartEntity.cartCreate(member);
        cartRepository.save(cart);


        Optional<ItemEntity> itemid = itemRepository.findByNo(itemEntity.getNo());
        ItemEntity item = itemid.get();

        CartItemEntity cartItemEntity = cartItemRepository.findByItemNoAndCartNo(item.getNo(), cart.getNo());

        if (cartItemEntity == null) {
            cartItemEntity = CartItemEntity.addCartItem(item, cart);
            cartItemRepository.save(cartItemEntity);
        }

    }

    public MemberDto memberDtoSearch(Long memberNo){

        Optional<MemberEntity> member = memberRepository.findByNo(memberNo);
        if(member.isPresent()){
            MemberDto memberDto = MemberDto.updateMemberDto(member.get());
            return memberDto;
        }else {
            return null;
        }

    }

    public CartEntity memberCartView(Long memberNo){
        Optional<MemberEntity> memberEntity= memberRepository.findByNo(memberNo);
        return cartRepository.findByMemberNo(memberEntity.get().getNo());
    }



    public ItemEntity itemSearch(Long no){
        Optional<ItemEntity> item = itemRepository.findByNo(no);
        ItemEntity itemEntity = item.get();
        return itemEntity;

    }

    public MemberEntity memberSearch(Long no){
        Optional<MemberEntity> member = memberRepository.findByNo(no);
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
         return memberEntity;

        }else{
            return null;
        }
    }



        public List<CartItemDto> cartVIew(){
        
        List<CartItemDto> cartItemDto = new ArrayList<>();

           List<CartItemEntity> cartItemEntities = cartItemRepository.findAll();

           for(CartItemEntity cartItem : cartItemEntities){
                ItemEntity itemEntity =cartItem.getItem();

               cartItemDto.add(CartItemDto.cartItemDto(cartItem,itemEntity));
           }
            return cartItemDto;
        }






        //장바구니 안에 특정 아이템 삭제
        public void cartItemDelete(Long cartItemNo){
            CartItemEntity cartItem= cartItemRepository.deleteByNo(cartItemNo);
        }



}