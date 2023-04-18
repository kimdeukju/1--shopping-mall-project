package org.spring.teamproject.chatbot;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//메시지 브로커
// 송신자의 메시지 프로토콜 형식으로부터의 메시지를 수신자의 메시지 프로토콜 형식으로 변환하는 중간 컴퓨터 프로그램 모듈이다.

@Configuration
@EnableWebSocketMessageBroker //메시지 브로커가 지원하는 WebSocket 메시지 처리를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메모리 기반 메시지 브로커가
    // 접두사가 붙은 목적지에서 클라이언트에게 인사말 메시지를 다시 전달할 수 있도록 호출하는 것
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 받을 때 관련 경로 설정
        config.enableSimpleBroker("/topic");
        // 구독 경로의 prefix 설정 -> 실제 경로 /app/topic
        // 클라이언트  prefix app
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 소켓 연결-> 앤드포인트
        registry.addEndpoint("/chatWebsocket").withSockJS();
    }

}