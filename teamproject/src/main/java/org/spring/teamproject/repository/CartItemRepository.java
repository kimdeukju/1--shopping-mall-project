package org.spring.teamproject.repository;

import org.spring.teamproject.dto.CartItemDto;
import org.spring.teamproject.entity.CartEntity;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity , Long> {


    CartItemEntity findByItemNoAndCartNo(Long itemNo, Long cartNo);



    CartItemEntity deleteByNo(Long cartItemNo);



//    @Query(value = "select ci.*, i.price , i.producer from cart_item ci join item i on i.item_no = ci.item_no"
//            , nativeQuery = true)
//    List<CartItemDto> findAllItemList(Long cartNo);







}
