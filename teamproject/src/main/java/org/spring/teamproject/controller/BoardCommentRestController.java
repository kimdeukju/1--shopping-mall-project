package org.spring.teamproject.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.spring.teamproject.dto.BoardDto;
import org.spring.teamproject.dto.ReplyDto;
import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.ReplyEntity;
import org.spring.teamproject.service.BoardService;
import org.spring.teamproject.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detail")
@RequiredArgsConstructor
public class BoardCommentRestController {
    private final ReplyService replyService;
    private final BoardService boardService;



    //    댓글 작성
    @ResponseBody
    @PostMapping(value="/comment/{no}")
    public String showComment(@PathVariable("no") Long no, @RequestBody String replyDto) {
        BoardEntity boardEntity = BoardEntity.builder()
                .no(no)
                .build();
        ReplyDto result = new ReplyDto();
        System.out.println("This is the username in replydto :"+replyDto);
        result.setBoardId(boardEntity);
//        ReplyDto가 ajax에서 stringify되면서 {"boardId":{no}, "re-content":{comment}} 전체가 들어간다.
//        따라서 해당 데이터에서 split을 이용해서 넣어주도록 하자
        String actualComment = replyDto.split("\"")[7];
        String re_writer = replyDto.split("\"")[11];
        result.setRe_writer(re_writer);
        result.setRe_content(actualComment);
        replyService.writeComment(result);
        return replyDto;
    }


    //    댓글 삭제
    @PostMapping("/commentDelete/{id}")
    public void deleteComment(@PathVariable("id") Long id) {
        System.out.println("This is the id of the deleting comment :"+id);

        ReplyDto reply = replyService.findComment(id);
        if (reply != null) {
            replyService.deleteComment(id);
            System.out.println("Deletion went successful.");
        }
    }

    // 댓글 수정
    @ResponseBody
    @PostMapping("/commentUpdate/{id}/{boardId}")
    public void updateComment(@RequestBody String replyDto, @PathVariable("id") Long id, @PathVariable("boardId") Long no) {
        ReplyDto reply = replyService.findComment(id);
        BoardEntity boardEntity = BoardEntity.builder().no(no).build();

        reply.setBoardId(boardEntity);
        reply.setId(id);
        System.out.println("This is the updated data"+replyDto);

//        ReplyDto가 ajax에서 stringify되면서 {"boardId":{no}, "re-content":{comment}} 전체가 들어간다.
//        따라서 해당 데이터에서 split을 이용해서 넣어주도록 하자
        String actualComment = replyDto.split("\"")[11];
        String commentWriter = replyDto.split("\"")[12];
        reply.setRe_content(actualComment);
        reply.setRe_writer(commentWriter);
        if (reply != null) {
            replyService.writeComment(reply);
        }
    }

}
