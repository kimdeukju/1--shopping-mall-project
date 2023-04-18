package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.BoardEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

  public Long no;

  @NotBlank(message = "title is required.")
  private String title;

  @NotBlank(message = "content is required.")
  private String content;

  private String writer;

  private String youtube;

  private int view;

  private int likes;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  //포함된 파일이 있으면 저장하는 객체
  private MultipartFile boardFile;

  private String originName;

  private String newName;

  private int attachFile;

  public static BoardDto entityToDto(BoardEntity boardEntity){

    BoardDto boardDto = new BoardDto();

    boardDto.setNo(boardEntity.getNo());
    boardDto.setTitle(boardEntity.getTitle());
    boardDto.setContent(boardEntity.getContent());
    boardDto.setWriter(boardEntity.getWriter());
    boardDto.setView(boardEntity.getView());
    boardDto.setYoutube(boardEntity.getYoutube());
    boardDto.setCreateTime(boardEntity.getCreateTime());
    boardDto.setUpdateTime(boardEntity.getUpdateTime());

    if(boardEntity.getAttachFile()==0){
      boardDto.setAttachFile(boardEntity.getAttachFile());
    }else{
      boardDto.setAttachFile(boardEntity.getAttachFile());
      boardDto.setOriginName(boardEntity.getFileEntities().get(0).getOriginName());
      boardDto.setNewName(boardEntity.getFileEntities().get(0).getNewName());
    }
    return boardDto;

  }

}
