package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.CartEntity;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.MemberEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDto {


    private Long id;
    private MemberEntity cart_member;
    private CartItemEntity cartItem;


//    public static CartDto cartDto(CartEntity cartEntity){
//        CartDto cartDto = new CartDto();
//        cartDto.setId(cartEntity.getNo());
//        cartDto.setCart_member(cartEntity.getCart_member());
//        cartDto.setCartItem(cartEntity.);
//
//    }
}
