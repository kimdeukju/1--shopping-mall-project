package org.spring.teamproject.entity;

import lombok.*;
import org.spring.teamproject.dto.BoardDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString
@Table(name = "board_tb")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_no", nullable = false)
	public Long no;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 10000)
	private String content;

	@Column(nullable = false)
	private String writer;

	private String youtube;

	@Column(columnDefinition = "integer default 0", nullable = false)
	private int view;

	@Column(columnDefinition = "integer default 0", nullable = false)
	private int likes;

	@Column(nullable = false)
	private int attachFile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_no")
	private MemberEntity boardJoinMember;

	//파일은 여러 개가 첨부될 수 있으니 List로 받는다
	@OneToMany(mappedBy = "fileJoinBoard", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<BoardFileEntity> fileEntities = new ArrayList<>();

	@OneToMany(mappedBy = "boardId", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ReplyEntity> replyEntities;

	public static BoardEntity dtoToEntityWithFile(BoardDto boardDto){

		BoardEntity boardEntity = new BoardEntity();

		boardEntity.setTitle(boardDto.getTitle());
		boardEntity.setContent(boardDto.getContent());
		boardEntity.setWriter(boardDto.getWriter());
		boardEntity.setYoutube(boardDto.getYoutube());
		boardEntity.setAttachFile(1);

		return boardEntity;

	}

	public static BoardEntity dtoToEntity(BoardDto boardDto){

		BoardEntity boardEntity = new BoardEntity();

		boardEntity.setTitle(boardDto.getTitle());
		boardEntity.setContent(boardDto.getContent());
		boardEntity.setWriter(boardDto.getWriter());
		boardEntity.setYoutube(boardDto.getYoutube());
		boardEntity.setAttachFile(0);

		return boardEntity;

	}

}
