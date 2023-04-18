let stompClient = null;

$(function () {
	// keyup이벤트
	$("#question").keyup(questionKeyuped);
});

function openChat() {
	setConnectStated(true); // 잡속 버튼 클릭시
	connect(); // 접속
}

function showMessage(message) {
	// 메시지를  #chat-content 에 추가
	$("#chat-content").append(message);
	//대화창 스크롤을 항상 최하위에 배치
	$("#chat-content").scrollTop($("#chat-content").prop("scrollHeight"));
}

// 잡속 버튼 클릭시 토글
function setConnectStated(isTrue) {
	if (isTrue) {
		$("#btn-chat-open").hide();
		$("#chat-disp").show();
	} else {
		$("#btn-chat-open").show();
		$("#chat-disp").hide();
	}
	$("#chat-content").html("");
}

// 접속 해제
function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnectStated(false);
	console.log("Disconnected");
}

//접속버튼클릭시 접속
//처음 접속시
function connect() {
	let socket = new SockJS("/chatWebsocket"); // 접속 URL
	stompClient = Stomp.over(socket);

	// 서버에 실제 전송
	stompClient.connect({}, function (frame) {
		console.log("Connected: " + frame);
		//브라우저에서 메시지를 수신하려면 STOMP 클라이언트가 먼저 대상을 구독
		//subscribe()방법을 사용하여 대상에 가입 -> 구독
		//2개의 필수 인수를 사용. destination목적지에 해당하는 문자열, callback,
		stompClient.subscribe("/topic/serverGo", function (botMessage) {
			// 클라이언트에서 메시지 보낼때
			showMessage(JSON.parse(botMessage.body).message);
		});

		stompClient.subscribe("/topic/message", function (botMessage) {
			// 클라이언트에게 메시지 보낼때
			showMessage(JSON.parse(botMessage.body).message);
		});

		// @MessageMapping("/app/msgTo")          //content -> ClientMessage content
		stompClient.send("/app/msgTo", {}, JSON.stringify({ content: "guest" }));
	});
}

//엔터키 -> 전송버튼 클릭이되면 질문을 텍스트 화면에 표현 되는 내용
function inputTagString(text) {
	let now = new Date();
	let ampm = now.getHours() > 11 ? "오후" : "오전";
	let time = ampm + (now.getHours() % 12) + ":" + now.getMinutes();
	//        리터럴 템플릿  ``
	// {text} 입력 텍스트
	let message = `
		<div class="msg user flex end" style="justify-content: right">
			<div class="message">
				<div class="part"  style="text-align: right">
					<p style="margin: 0">${text}</p>
				</div>
				<div class="time" style="text-align: right">${time}</div>
			</div>
		</div>
	`;
	return message;
}
//메뉴클릭시 메뉴 텍스트 화면에 표현
function menuclicked(el) {
	let text = $(el).text().trim();
	let message = inputTagString(text);
	showMessage(message);
	// 서버에서 전송된 메시지
	// prefix app
	stompClient.send("/app/message", {}, JSON.stringify({ content: text }));
}

//엔터가 입력이되면 질문을 텍스트 화면에 표현
function questionKeyuped(event) {
	//엔터 키
	if (event.keyCode != 13) return;
	btnMsgSendClicked();
}

//엔터키 -> 전송버튼 클릭이되면 질문을 텍스트 화면에 표현
function btnMsgSendClicked() {
	let question = $("#question").val().trim();
	// 빈문자열이나 2글자 미만이면 return
	if (question == "" || question.length < 2) return;
	// 전송 메시지 -> 화면에 출력
	let message = inputTagString(question);
	showMessage(message);
	// 전송 메시지 -> 화면에 출력 후 공란
	$("#question").val("");
	// 전송 메시지 -> 서버에 전송 -> JSON형태고 전송
	// prefix app
	stompClient.send("/app/message", {}, JSON.stringify({ content: question }));
}
