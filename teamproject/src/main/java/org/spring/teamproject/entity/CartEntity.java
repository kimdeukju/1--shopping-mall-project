package org.spring.teamproject.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_no")
    private Long no;

    @OneToOne
    @JoinColumn(name = "member_no")
    private MemberEntity member;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.REMOVE)
    private List<CartItemEntity> cartItemEntities = new ArrayList<>();

public static CartEntity cartCreate( MemberEntity memberEntity){
        CartEntity cartEntity = new CartEntity();
        cartEntity.setMember(memberEntity);
        return cartEntity;

    }
}
