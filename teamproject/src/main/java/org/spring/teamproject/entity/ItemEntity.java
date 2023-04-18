package org.spring.teamproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.teamproject.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class ItemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_no")
    private Long no;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private int bpm;

    @Column(nullable = false)
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private MemberEntity item_member;


    //선택사항
    @OneToMany(mappedBy = "item",cascade = CascadeType.REMOVE)
    private List<CartItemEntity> itemEntityList = new ArrayList<>();


    public static ItemEntity toItemEntity(ItemDto itemDto) {

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle(itemDto.getTitle());
        itemEntity.setDuration(itemDto.getDuration());
        itemEntity.setProducer(itemDto.getProducer());
        itemEntity.setPrice(itemDto.getPrice());
        itemEntity.setBpm(itemDto.getBpm());
        itemEntity.setGenre(itemDto.getGenre());
        itemEntity.setItem_member(itemDto.getMember());

        return itemEntity;
    }
    public static ItemEntity toItemAllEntity(ItemDto itemDto) {

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setNo(itemDto.getNo());
        itemEntity.setTitle(itemDto.getTitle());
        itemEntity.setDuration(itemDto.getDuration());
        itemEntity.setProducer(itemDto.getProducer());
        itemEntity.setPrice(itemDto.getPrice());
        itemEntity.setBpm(itemDto.getBpm());
        itemEntity.setGenre(itemDto.getGenre());
        itemEntity.setItem_member(itemDto.getMember());

        return itemEntity;
    }






}
