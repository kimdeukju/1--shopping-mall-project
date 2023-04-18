package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.BoardFileEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardFileDto {

	public Long no;

	private String originName;

	private String newName;

	private BoardEntity fileJoinBoard;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
