package org.spring.teamproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_no")
    private Long no;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_no")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private ItemEntity item;

public static CartItemEntity addCartItem ( ItemEntity itemEntity,CartEntity cartEntity){
        CartItemEntity cartItemEntity = new CartItemEntity();

        cartItemEntity.setItem(itemEntity);
        cartItemEntity.setCart(cartEntity);

        return cartItemEntity;


    }

}
