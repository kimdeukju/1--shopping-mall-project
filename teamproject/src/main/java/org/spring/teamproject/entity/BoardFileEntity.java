package org.spring.teamproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@ToString
@Table(name = "boardFile_tb")
@AllArgsConstructor
@NoArgsConstructor
public class BoardFileEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "boardFile_no", nullable = false)
  public Long no;

  private String originName;

  private String newName;

  @ManyToOne
  @JoinColumn(name = "board_no")
  private BoardEntity fileJoinBoard;

  public static BoardFileEntity fileUpload(BoardEntity boardEntity, String originName, String newName){

    BoardFileEntity fileEntity = new BoardFileEntity();

    fileEntity.setFileJoinBoard(boardEntity);
    fileEntity.setOriginName(originName);
    fileEntity.setNewName(newName);

    return fileEntity;

  }

}
