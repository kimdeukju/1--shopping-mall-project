package org.spring.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.ItemDto;
import org.spring.teamproject.entity.ItemEntity;
import org.spring.teamproject.repository.FileRepository;
import org.spring.teamproject.repository.ItemRepository;
//import org.spring.teamproject.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileRepository fileRepository;
//    private final MemberRepository memberRepository;

    // 트랙 추가
    @Transactional
    public void itemInsert(ItemDto itemDto)  {

            ItemEntity itemEntity=ItemEntity.toItemEntity(itemDto);
            itemRepository.save(itemEntity);

    }

    public List<ItemDto> itemList() {
        List<ItemDto> itemDtoList=new ArrayList<>();

        List<ItemEntity> itemEntityList=itemRepository.findAll();

        for(ItemEntity itemEntity:itemEntityList){
            itemDtoList.add(ItemDto.toItemDto(itemEntity));
        }
        return itemDtoList;
    }
    // 트랙 목록 페이징
    public Page<ItemDto> itemPage(Pageable pageable) {
        Page<ItemEntity> itemEntities=itemRepository.findAll(pageable);


        Page<ItemDto> itemDtoPage=itemEntities.map(ItemDto::toItemDto);

        return itemDtoPage;
    }
    // 목록에서 검색
    public Page<ItemDto> search(String search,Pageable pageable) {
        Page<ItemEntity> itemEntities=itemRepository.findByTitleContaining(search,pageable);

        Page<ItemDto> itemDtoPage=itemEntities.map(ItemDto::toItemDto);

        return itemDtoPage;



    }

    //트랙 상세목록
    public ItemDto trackDetail(long no) {
        Optional<ItemEntity> itemEntity=itemRepository.findByNo(no);

        if(itemEntity.isPresent()){
            ItemDto itemDto=ItemDto.toItemDto(itemEntity.get());

            return itemDto;
        }else{
            return null;
        }
    }

    //트랙 상세목록 10개
    public List<ItemDto> trackDetailRandom(long no) {
        List<ItemDto> itemDtoList=new ArrayList<>();

        List<ItemEntity> itemEntityList=itemRepository.findTop8ByNoNotOrderByNoAsc(no);

        for(ItemEntity itemEntity:itemEntityList){
            itemDtoList.add(ItemDto.toItemDto(itemEntity));
        }
        return itemDtoList;


    }

    //트랙 수정
    public ItemDto trackUpdate(long no) {
        Optional<ItemEntity> itemEntity=itemRepository.findByNo(no);

        if(itemEntity.isPresent()){
            return ItemDto.toItemDto(itemEntity.get());
        }else{
            return null;
        }
    }
    // 트랙 수정 실행
    @Transactional
    public void trackUpdateOk(ItemDto itemDto)  {

        ItemEntity itemEntity=ItemEntity.toItemAllEntity(itemDto);
        itemRepository.save(itemEntity);
    }

    @Transactional
    public void trackDelete(Long no) {

        itemRepository.deleteByNo(no);
    }

}

































