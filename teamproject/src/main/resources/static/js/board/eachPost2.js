// 댓글에 대한 수정, 삭제를 해당 댓글을 쓴사람만 처리할 수 있게 하기
const hide1 = 'hide';


let CommentWriter = document.querySelectorAll('#re_writer');
let memberActionCon = document.querySelectorAll('.memberAction');

if(localStorage.length == 0){
  memberActionCon.forEach((el)=>{
    el.classList.add(hide1);
  })
} else {
  CommentWriter.forEach((el, ind)=>{
    if(el.value == localStorage.getItem("currentPrincipal")){
      memberActionCon.forEach((el, index)=>{
        if(ind == index){
          el.classList.remove(hide1);
      }
    })
    }
  });
}
// -------------------------------------------------------------------------------
// ajax를 사용하여 댓글 입력하기


function commentInsertFn(e) {

  let detailPost = document.querySelector('#boardNo').value;
  let commentData = document.querySelector('#commentsInputData').value;
 let userNameString = localStorage.getItem("currentPrincipal");
  const sendDto = {
    boardId: detailPost,
    re_content: commentData,
    re_write: userNameString
  };

  console.log(`This is the data of detailpost : ${detailPost}`);
  // console.log(`This is the data of commentData : ${commentData}`);
  console.log(`This is the sendDto in ajax : ${JSON.stringify(sendDto)}`);


  $.ajax({
    type: 'post',
    url: `/detail/comment/${detailPost}`,
    data: JSON.stringify(sendDto),
    contentType: "application/text; charset=utf-8",
    success: function onData(data1) {
      console.log("Success");
      $(".comment-lists").append('<ul class="lists" th:each="comment:${detailComments}">'
        + '<li class="child">'
        + '<ul id="comments">'
        + '<li>'
        + '<input id="No" name="id" th:value="${comment.id}" readonly>'
        + '</li>'
        + '<li>'
        + '<textarea id="Re_content" name="re_content" th:text="${comment.re_content}" rows="4" cols="20">' + commentData + '</textarea>'
        + '</li>'
        + '<li>'
        + '<input id="CreateTime" th:value="${comment.createTime}" readonly>'
        + '</li>'
        + '<li>'
        + '<button id="update">Update</button>'
        + '</li>'
        + '<li>'
        + '<button type="button" id="delete" th:formaction="@{|/detail/commentDelete/${comment.id}|}">Delete</button>'
        + '</li>'
        + '</ul>'
        + '</li>'
        + '</ul>');
      // 댓글 입력 시, 해당 페이지 내에서 새로고침이 일어난다.
      // 새로고침을 하지 않고 보여줄 수 있는 방법 고심 필요
      location.replace(`http://localhost:8099/board/detail/${detailPost}`);
    },
    errror: function onError(error) {
      alert(error);
    }
  })
}

document.querySelector('.commentSubmit').addEventListener('click', commentInsertFn);

// -------------------------------------------------------------------------------
// ajax를 사용하여 댓글을 바로 삭제하기
function commentDeleteFn(e) {
  let commentId = e.target.closest('#comments').querySelector('#No').value;
  let detailPost = document.querySelector('#boardNo').value;



  $.ajax({
    type: 'post',
    url: `/detail/commentDelete/${commentId}`,
    success: function onData(data1) {
      alert('댓글 삭제가 완료되었습니다.');
      location.replace(`http://localhost:8099/board/detail/${detailPost}`);
    },
    errror: function onError(error) {
      console.log(error);
    }
  })

};



document.querySelectorAll('#comments>li:nth-child(4)>ul>li:nth-child(2)').forEach((el, index) => {
  el.addEventListener('click', commentDeleteFn);
});


// -------------------------------------------------------------------------------
// ajax를 사용하여 댓글 수정하기
function commentUpdateFn(e) {
  console.log(`e.currentTarget: ${e.currentTarget}`);
  console.log(`This shows parent element of e.currentTarget: ${e.currentTarget.parentElement.id}`);
  console.log(`This shows the id of the selected comment: ${e.target.closest('#comments').querySelector('#No').value}`);


  let commentId = e.target.closest('#comments').querySelector('#No').value;
  let detailPost = document.querySelector('#boardNo').value;
  let commentData = e.target.closest('#comments').querySelector('#Re_content').value;
  let commentWriter = e.target.closest('#comments').querySelector('#re_writer').value;
  const sendDto = {
    id: commentId,
    boardId: detailPost,
    re_content: commentData,
    re_writer: commentWriter
  };
  $.ajax({
    type: 'post',
    url: `/detail/commentUpdate/${commentId}/${detailPost}`,
    data: JSON.stringify(sendDto),
    contentType: "application/text; charset=utf-8",
    success: function onData(data1) {
      alert('댓글 수정이 완료되었습니다.');
      location.replace(`http://localhost:8099/board/detail/${detailPost}`);
    },
    errror: function onError(error) {
      console.log(error);
    }
  })

};



document.querySelectorAll('#comments>li:nth-child(4)>ul>li:nth-child(1)').forEach((el, index) => {
  el.addEventListener('click', commentUpdateFn);
});




//---------------------------------------------------------------------------
// 2. 이 코드는 Iframe Player API를 비동기적으로 로드한다. !!필수!!
var tag = document.createElement('script');
let youtubeSource = document.querySelector('#youtubeSource');


// youtubeSource의 값에서 0을 받으면 해당 함수를 실행하지 않음

if (youtubeSource.value != 0) {

  tag.src = "https://www.youtube.com/iframe_api";
  var firstScriptTag = document.getElementsByTagName('script')[0];
  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

  // 3. API 코드를 다운로드 받은 다음에 <iframe>을 생성하는 기능 (youtube player도 더불어)
  var player;
  function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
      height: '200px',  //변경가능-영상 높이
      width: '300px',  //변경가능-영상 너비
      videoId: youtubeSource.value,  //변경-영상ID
      playerVars: {
        'rel': 1,    //연관동영상 표시여부(0:표시안함)
        'controls': 1,    //플레이어 컨트롤러 표시여부(0:표시안함)
        'autoplay': 0,   //자동재생 여부(1:자동재생 함, mute와 함께 설정)
        'mute': 0,   //음소거여부(1:음소거 함)
        'loop': 1,    //반복재생여부(1:반복재생 함)
        'playsinline': 1,    //iOS환경에서 전체화면으로 재생하지 않게
        'playlist': youtubeSource.value   //재생할 영상 리스트
      },
      events: {
        'onReady': onPlayerReady, //onReady 상태일 때 작동하는 function이름
        'onStateChange': onPlayerStateChange //onStateChange 상태일 때 작동하는 function이름
      }
    });
  }
  // 4. API는 비디오 플레이어가 준비되면 아래의 function을 불러올 것이다.
  function onPlayerReady(event) {
    event.target.playVideo();
  }

  // 5. API는 플레이어의 상태가 변화될 때 아래의 function을 불러올 것이다.
  //    이 function은 비디오가 재생되고 있을 때를 가르킨다.(state=1),
  //    플레이어는 6초 이상 재생되고 정지되어야 한다.
  var done = false;
  function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING && !done) {
      // setTimeout(stopVideo, 0);
      done = true;
    }
  }
  function stopVideo() {
    player.stopVideo();
  }
}

