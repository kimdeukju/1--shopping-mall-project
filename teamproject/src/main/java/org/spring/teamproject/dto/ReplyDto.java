package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.BoardEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDto extends BaseTimeDto{

    private Long id;

    private String re_content;

    private String re_writer;

    private BoardEntity boardId;

//    BaseTimeDto의 createTime 필드를 가져오기 위해 여기에 선언함
    LocalDateTime createTime;

    public ReplyDto(Long id, String re_content, LocalDateTime createTime, String re_writer){
        this.id=id;
        this.re_content=re_content;
        this.createTime=createTime;
        this.re_writer=re_writer;
    }

}
