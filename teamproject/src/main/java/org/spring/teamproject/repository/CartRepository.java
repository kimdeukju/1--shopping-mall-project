package org.spring.teamproject.repository;

import org.spring.teamproject.entity.CartEntity;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface CartRepository extends JpaRepository<CartEntity,Long> {


    Optional<CartEntity> findByNo(Long cartNo);

    CartEntity findByMemberNo(Long memberNo);

//    Optional<CartEntity> findByMemberNo(Long memberNo);
//    @Query(value = "select * from cart where member_no=? ",nativeQuery = true)
//    CartEntity findByMemberNo(@Param("member_no") Long memberNo);

//    CartEntity findByMemberNo(Long no);
}
