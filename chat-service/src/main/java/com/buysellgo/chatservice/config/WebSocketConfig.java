package com.buysellgo.chatservice.config;

// Spring의 Configuration 어노테이션을 사용하여 설정 클래스로 지정
import org.springframework.context.annotation.Configuration;
// 메시지 브로커 설정을 위한 클래스
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// WebSocket 메시지 브로커를 활성화하기 위한 어노테이션
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// STOMP 엔드포인트 등록을 위한 클래스
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// WebSocket 메시지 브로커 설정을 위한 인터페이스
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// 이 클래스는 WebSocket 메시지 브로커를 설정하는 데 사용됩니다.
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커를 구성하는 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // enableSimpleBroker(): 내장 메시지 브로커를 사용하여 클라이언트에게 메시지를 전달
        // setApplicationDestinationPrefixes(): 클라이언트에서 서버로 메시지를 보낼 때 사용할 prefix 설정
        // enableStompBrokerRelay(): 외부 메시지 브로커(RabbitMQ, ActiveMQ 등)를 사용하여 메시지를 전달
        // setUserDestinationPrefix(): 특정 사용자에게 메시지를 보낼 때 사용할 prefix 설정 ("/user/")
        // setPreservePublishOrder(): 메시지 발행 순서 보장 여부 설정 (true/false)
        config.enableSimpleBroker("/topic", "/queue");
        // 외부 메세지 브로커(카프카)를 사용하는 경우에는, consumer와 porducer를 서비스에서 따로 구현하고 전달하기 때문에 /app 경로로 보내게 된다.
        config.setApplicationDestinationPrefixes("/app");
    }

    // STOMP 엔드포인트를 등록하는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws" 엔드포인트를 추가하고, 모든 출처에서의 요청을 허용하며, SockJS를 사용하도록 설정
        // 모든 출처에서의 요청을 허용하는 이유는 이미 게이트웨이에서 cors 설정을 했기 때문
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
} 