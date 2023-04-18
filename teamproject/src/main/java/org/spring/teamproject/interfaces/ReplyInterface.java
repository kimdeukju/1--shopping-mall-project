package org.spring.teamproject.interfaces;

import org.spring.teamproject.dto.BoardDto;
import org.spring.teamproject.dto.ReplyDto;
import org.spring.teamproject.entity.BoardEntity;

import java.util.List;

public interface ReplyInterface {

    List<ReplyDto> showCommentsOfDetailPost(BoardDto boardDto);

    void writeComment(ReplyDto replyDto);

    ReplyDto findComment(Long id);

    void deleteComment(Long id);
}
