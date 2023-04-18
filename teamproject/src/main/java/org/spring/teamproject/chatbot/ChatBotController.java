package org.spring.teamproject.chatbot;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class ChatBotController {

    @MessageMapping("/msgTo") // 실제 -> /app/msgTo
    @SendTo("/topic/serverGo")//stompClient.subscribe
    public BotMessage serverGo(ClientMessage message) throws Exception {
        Thread.sleep(300); // 0.5초 delay
        LocalDateTime today=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy MM, dd");
        String formattedDay=today.format(formatter);
        String formattedtime=today.format(DateTimeFormatter.ofPattern("a H:mm").withLocale(Locale.forLanguageTag("en")));

        // 초기 메시지
        return new BotMessage(
                "<div class='flex center date' >"+formattedDay+"</div>"+
                        "<div class='msg bot flex'>"+
                        "<div class='icon'>"+
                        "<img src='/img/common/chatBot/chat.png'  th:alt=\"#{chat}\" />" +
                        "</div>"+
                        "<div class='message'>"+
                        "<div class='part'>"+
                        "<p>Hello, this is Chat Bot. Ask me any questions!</p>"+
                        "</div>" +
                        "<div class='part'>"+
                        "<p>Please click Frequently Asked Questions.</p>"+
                        "<div class='flex center menu'>"+
                        "<div class='menu-item'><span onclick='menuclicked(this)'>Track Inquiry</span></div>"+
                        "<div class='menu-item'><span onclick='menuclicked(this)'>Payment Inquiry</span></div>"+
                        "<div class='menu-item'><span onclick='menuclicked(this)'>Artist Register Inquiry</span></div>"+
                        "</div>"+
                        "</div>"+
                        "<div class='time'>"+
                        formattedtime+
                        "</div>"+
                        "</div>"+

                        "</div>");
    }


    @MessageMapping("/message")// 실제 -> /app/message
    @SendTo("/topic/message")//stompClient.subscribe
    public BotMessage message(ClientMessage message) throws Exception {
//        Thread.sleep(1); // simulated delay
        LocalDateTime today=LocalDateTime.now();
        String formattedtime=today.format(DateTimeFormatter.ofPattern("a H:mm").withLocale(Locale.forLanguageTag("en")));

        String responseText=message.getContent()+" This is the reply.";
        //클라이언트 메시지  /topic/message 요청시
        return new BotMessage(
                "<div class='msg bot flex'>"+
                        "<div class='icon'>"+
                        "<img src='/img/common/chatBot/chat.png'  th:alt=\"#{chat}\" />" +
                        "</div>"+
                        "<div class='message'>"+
                        "<div class='part'>"+
                        "<p>"+responseText+"</p>"+
                        "</div>"+
                        "<div class='time'>"+
                        formattedtime+
                        "</div>"+
                        "</div>"+
                        "</div>");
    }

}
