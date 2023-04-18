package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.CartItemEntity;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {

    private Long no;
    private String title; //track title
    private String duration; //불확실
    private String producer;
    private String price; //$
    private int bpm;
    private String genre; //장르
    private MemberEntity member;
    private CartItemEntity cartItem;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;



    public static ItemDto toItemDto(ItemEntity itemEntity) {

        ItemDto itemDto = new ItemDto();

        itemDto.setNo(itemEntity.getNo());
        itemDto.setTitle(itemEntity.getTitle());
        itemDto.setDuration(itemEntity.getDuration());
        itemDto.setBpm(itemEntity.getBpm());
        itemDto.setProducer(itemEntity.getProducer());
        itemDto.setGenre(itemEntity.getGenre());
        itemDto.setPrice(itemEntity.getPrice());
        itemDto.setCreateTime(itemEntity.getCreateTime());
        itemDto.setMember(itemEntity.getItem_member());

        return itemDto;
    }


}
