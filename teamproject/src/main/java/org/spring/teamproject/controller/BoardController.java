package org.spring.teamproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.teamproject.config.UserDetailSecurity;
import org.spring.teamproject.dto.BoardDto;
import org.spring.teamproject.dto.BoardFileDto;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.dto.ReplyDto;
import org.spring.teamproject.entity.BoardFileEntity;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.service.BoardService;
import org.spring.teamproject.service.MemberService;
import org.spring.teamproject.service.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final MemberService memberService;
  private final ReplyService replyService;

  // 게시글 메인페이지 이동
  @GetMapping({"","/main","/list"})
  public String boardMain(Model model, @PageableDefault(page = 0, size = 15, sort = "no", direction = Sort.Direction.DESC) Pageable pageable){




    Page<BoardDto> boardList = boardService.boardListView(pageable);
    
    int blockNum = 5; //화면에 보여지는 페이지 인덱스 블록의 갯수 < 1, 2, 3, 4... > <- 이거
    int nowPage = boardList.getNumber() + 1; //현재 페이지
    int startPage = Math.max(1, boardList.getNumber()-blockNum); //시작 페이지
    int endPage = boardList.getTotalPages(); //마지막 페이지

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    model.addAttribute("boardList", boardList);

    return "pages/board/list";
  }


// 게시글 Paging
  @GetMapping("/search")
  public String boardSearch(@RequestParam(value = "search", required = false) String search,
                            @PageableDefault(page = 0, size = 10, sort = "no", direction = Sort.Direction.DESC) Pageable pageable,
                            Model model){

    Page<BoardDto> searchList = boardService.searchListView(pageable, search);

    int blockNum = 5;
    int nowPage = searchList.getNumber() + 1;
    int startPage = Math.max(1, searchList.getNumber()-blockNum);
    int endPage = searchList.getTotalPages();

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    model.addAttribute("boardList", searchList);

    return "pages/board/list";
  }

//  게시글 작성 페이지 이동
// security의 사용자 정보를 가져간다
  @GetMapping("/write")
  public String boardWrite(@AuthenticationPrincipal UserDetails user, Model model, BoardDto boardDto) {
    //시큐리티에 저장된 회원정보 가져가기

    String loginUser = user.getUsername();
    // 로그인 아이디를 따로 저장해 둔다.
    


    model.addAttribute("userName", loginUser);
    model.addAttribute("boardDto", new BoardDto());
    return "pages/board/write";
  }
//  게시글 작성 실행
  @PostMapping("/write")
  public String boardWritePost(BoardDto boardDto) throws IOException {

    boardService.boardWrite(boardDto);

    return "redirect:/board/list";
  }

  // 게시글 상세 조회 하기
  @GetMapping("/detail/{no}")
  public String boardDetail(@PathVariable Long no, Model model) {

    boardService.upViewCount(no); //조회수 증가 처리
    BoardDto boardDto = boardService.boardDetailView(no);

//    @AuthenticationPrincipal UserDetails user
//    String loginUser = user.getUsername();

    if(boardDto!=null){

      //    유튜브 영상이 있으면, 해당 링크의 아이디를 View로 보내고, 아니면 안한다.
      if (boardDto.getYoutube().toString().length()>=1) {
        String youtubeSource = boardDto.getYoutube().toString().split("=")[1];
        model.addAttribute("youtubeSource", youtubeSource);
      } else {
        model.addAttribute("youtubeSource", 0);
      }
//      게시글 내용 전달
      model.addAttribute("detail", boardDto);

//    게시글에 대한 전체 댓글 리스트
      List<ReplyDto> replyDto = replyService.showCommentsOfDetailPost(boardDto);
      model.addAttribute("detailComments", replyDto);
      return "pages/board/detailView";
    }else{
      //게시글이 삭제되었거나 존재하지 않습니다 창 띄우기
      return "redirect:/board/list";
    }
  }

  //게시글 수정 페이지 이동
  @GetMapping("/update/{no}")
  public String boardUpdate(@PathVariable Long no, Model model){

    BoardDto boardDto = boardService.boardDetailView(no);
    model.addAttribute("detail", boardDto);

    return "pages/board/updateView";
  }

  // 게시글 수정 실행
  @PostMapping("/update/{no}")
  public String boardUpdatePost(@PathVariable Long no, BoardDto boardDto) throws IOException {

    boardService.boardUpdate(no, boardDto);
    return "redirect:/board/detail/{no}";
  }

  //게시글 삭제
  @GetMapping("/delete/{no}")
  public String boardDelete(@PathVariable Long no){
    //삭제하시겠습니까? 창 띄우기 -> js
    boardService.boardDelete(no);
    return "redirect:/board/";
  }




}