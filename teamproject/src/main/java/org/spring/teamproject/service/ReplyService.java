package org.spring.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.BoardDto;
import org.spring.teamproject.dto.ReplyDto;
import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.ReplyEntity;
import org.spring.teamproject.interfaces.ReplyInterface;
import org.spring.teamproject.repository.BoardRepository;
import org.spring.teamproject.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService implements ReplyInterface {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Override
    public List<ReplyDto> showCommentsOfDetailPost(BoardDto boardDto) {
        BoardEntity boardEntityNo=new BoardEntity();
        boardEntityNo.setNo(boardDto.getNo());
        boardEntityNo.setTitle(boardDto.getTitle());
        boardEntityNo.setContent(boardDto.getContent());
        boardEntityNo.setWriter(boardDto.getWriter());
        boardEntityNo.setYoutube(boardDto.getYoutube());
        boardEntityNo.setView(boardEntityNo.getView());
        boardEntityNo.setLikes(boardDto.getLikes());
        boardEntityNo.setAttachFile(boardDto.getAttachFile());


//          ReplyEntity에서 가져온 값을, ReplyDto의 id, re_content를 인자로 가지는 생성자에 넣어, ReplyDto 타입의 List를 완성한다.
        List<ReplyEntity> replyEntities = replyRepository.findAllByBoardId(boardEntityNo);
        List<ReplyDto> replyDtos = new ArrayList<>();
        for(ReplyEntity i:replyEntities){
            ReplyDto replyDto1= new ReplyDto(i.getId(), i.getRe_content(), i.getCreateTime(), i.getRe_writer());
            replyDtos.add(replyDto1);
        }
        System.out.println("This shows comments :"+replyDtos);
        return replyDtos;
    }

    @Override
    public void writeComment(ReplyDto replyDto) {
        ReplyEntity replyEntity = new ReplyEntity();

        replyEntity.setId(replyDto.getId());
        replyEntity.setRe_content(replyDto.getRe_content());
        replyEntity.setBoardId(replyDto.getBoardId());
        replyEntity.setRe_writer(replyDto.getRe_writer());

        System.out.println("!!!!!!!!!!!!!!!!!!This is re_content in service:"+replyDto);


        if(replyRepository.save(replyEntity) != null){
            System.out.println("Writing comment went successful");
        }
    }


    @Override
    public ReplyDto findComment(Long id) {
        Optional<ReplyEntity> replyEntity=replyRepository.findById(id);
        ReplyDto replyDto1 = ReplyDto.builder()
                .id(replyEntity.get().getId())
                .re_content(replyEntity.get().getRe_content())
                .boardId(replyEntity.get().getBoardId())
                .createTime(replyEntity.get().getCreateTime())
                .build();
        return replyDto1;
    }

    @Override
    public void deleteComment(Long id) {
        replyRepository.deleteById(id);
    }
}
