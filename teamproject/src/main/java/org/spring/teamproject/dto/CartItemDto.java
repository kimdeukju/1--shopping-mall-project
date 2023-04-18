package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.CartEntity;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.ItemEntity;

import java.lang.ref.PhantomReference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItemDto {


    private Long no;
    private String  price;
    private String producer;


    private CartEntity cart;
    private ItemEntity item;

    public static CartItemDto cartItemDto (CartItemEntity cartItemEntity ,ItemEntity itemEntity){
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setNo(cartItemEntity.getNo());
        cartItemDto.setItem(itemEntity);
        cartItemDto.setProducer(itemEntity.getProducer());
        cartItemDto.setPrice(itemEntity.getPrice());

        return cartItemDto;
    }




}
