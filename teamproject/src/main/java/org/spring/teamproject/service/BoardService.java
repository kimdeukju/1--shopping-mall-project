package org.spring.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.dto.BoardDto;
import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.BoardFileEntity;
import org.spring.teamproject.repository.BoardRepository;
import org.spring.teamproject.repository.FileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final FileRepository fileRepository;

  //글 쓰기(파일업로드 처리O, 유튜브 링크 처리X)
  @Transactional
  public void boardWrite(BoardDto boardDto) throws IOException {

    if(boardDto.getBoardFile().isEmpty()){
      boardRepository.save(BoardEntity.dtoToEntity(boardDto));
    }else{
      MultipartFile boardFile = boardDto.getBoardFile();
      String originName = boardFile.getOriginalFilename();
      UUID uuid = UUID.randomUUID();
      String newName = uuid + "_" + originName;
      String filePath = "c:/fileFolder/" + newName;

      boardFile.transferTo(new File(filePath));

      BoardEntity boardEntity = BoardEntity.dtoToEntityWithFile(boardDto);
      Long boardId = boardRepository.save(boardEntity).getNo();

      Optional<BoardEntity> boardFind = boardRepository.findById(boardId);
      BoardEntity boardGet = boardFind.get();

      BoardFileEntity file = BoardFileEntity.fileUpload(boardGet, originName, newName);
      fileRepository.save(file);
    }
  }

  //게시글 목록(전체) 불러오기
  public Page<BoardDto> boardListView(Pageable pageable) {

    Page<BoardEntity> boardEntity = boardRepository.findAll(pageable);
    Page<BoardDto> boardDto = boardEntity.map(BoardDto::entityToDto);

//    for(BoardEntity forEntity : boardEntity){
//      boardDto.add(BoardDto.entityToDto(forEntity));
//    }

    return boardDto;
  }

  public Page<BoardDto> searchListView(Pageable pageable, String search) {
    Page<BoardEntity> boardEntity = boardRepository.findByTitleContaining(pageable, search);
    Page<BoardDto> boardDto = boardEntity.map(BoardDto::entityToDto);

    return boardDto;
  }

  //게시글 상세조회
  public BoardDto boardDetailView(Long no) {

    Optional<BoardEntity> optionalBoard = boardRepository.findById(no);

    if(optionalBoard.isPresent()){
      BoardDto boardDto = BoardDto.entityToDto(optionalBoard.get());
      return boardDto;
    }else{
      return null;
    }
  }

  //조회수 증가
  @Transactional
  public void upViewCount(Long no) {
    boardRepository.upViewCount(no);
  }
  
  //게시글 수정
  @Transactional
  public void boardUpdate(Long no, BoardDto boardDto) throws IOException {
    //파일이 없는 게시글이 파일을 추가하지 않은 채 글만 수정한 경우
    //파일이 없는 게시글이 파일을 추가하고 수정한 경우
    //파일이 있는 게시글이 파일을 변경하지 않은 채 글만 수정한 경우
    //파일이 있는 게시글이 파일을 삭제하고 수정한 경우

//    Optional<BoardEntity> optionalBoard = boardRepository.findById(no);
//    Optional<BoardFileEntity> optionalBoardFile = fileRepository.findByBoardId(no);
//
//    if(optionalBoard.isPresent()){
//      if(boardDto.getBoardFile().isEmpty()){
//        boardRepository.save(BoardEntity.dtoToEntity(boardDto));
//        fileRepository.delete(optionalBoardFile);
//      }else{
//        MultipartFile boardFile = boardDto.getBoardFile();
//        String originName = boardFile.getOriginalFilename();
//        UUID uuid = UUID.randomUUID();
//        String newName = uuid + "_" + originName;
//        String filePath = "c:/fileFolder/" + newName;
//
//        boardFile.transferTo(new File(filePath));
//
//        BoardEntity boardEntity = BoardEntity.dtoToEntityWithFile(boardDto);
//        Long boardId = boardRepository.save(boardEntity).getNo();
//
//        Optional<BoardEntity> boardFind = boardRepository.findById(boardId);
//        BoardEntity boardGet = boardFind.get();
//
//        BoardFileEntity file = BoardFileEntity.fileUpload(boardGet, originName, newName);
//        fileRepository.save(file);
//      }
//    }

  }
  
  //게시글 삭제
  @Transactional
  public void boardDelete(Long no) {

    Optional<BoardEntity> optionalBoard = boardRepository.findById(no);

    if(optionalBoard.isPresent()){
      boardRepository.delete(optionalBoard.get());
    }
  }

//

}
