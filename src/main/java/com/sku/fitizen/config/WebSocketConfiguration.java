package com.sku.fitizen.config;

import com.sku.fitizen.handler.AlarmWebSocketHandler;
import com.sku.fitizen.interceptor.HttpHandshakeInterceptor;
import com.sku.fitizen.handler.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer
{
    /* "ws://ip/ws/chat" 요청은 인터셉터를 거쳐 웹소켓 핸들러에 연결됨 */
    private final static String CHAT_ENDPOINT="/chat";
    private final static String TRAINER_ENDPOINT = "/tChat";
    private final static String ALARM_ENDPOINT = "/alarm";

   /* WebSocket 핸드셰이크
      지금 컨트롤러에 /trainer 가 있기때문에  TRAINER_ENDPOINT =/trainer 해버리면
      다음과 같은 이유 (GPT)

      경로 설정 겹침:
            HTTP 경로인 http://localhost/trainer와 WebSocket 경로인 ws://localhost/trainer가 겹칩니다. Spring이 어떤 요청을 처리해야 할지 결정할 때 혼란을 겪을 수 있습니다.

      핸들러 충돌:
            같은 경로에 HTTP 컨트롤러와 WebSocket 핸들러가 동시에 존재하면, Spring은 어떤 핸들러를 사용할지 명확히 알지 못하게 됩니다. 이로 인해 WebSocket 핸드셰이크가 실패하게 됩니다.

      WebSocket 핸드셰이크:
            WebSocket 연결을 위한 핸드셰이크가 HTTP 요청으로 시작되므로, 경로가 동일하면 핸드셰이크가 올바르게 처리되지 않을 수 있습니다.
    */

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //챌린지
        registry.addHandler(getChatWebSocketHandler(), CHAT_ENDPOINT)  /* 커스텀 웹소켓핸들러 등록*/
                .addInterceptors(new HttpHandshakeInterceptor())        /* 커스텀 인터셉터 등록 */
                .setAllowedOrigins("*");
        // 위에서 지정한 인터셉터를 통해 WebSocketHandler에게 필요한 속성들을 전달할 수 있다


        // 트레이너 WebSocket 핸들러
        registry.addHandler(getTrainerWebSocketHandler(), TRAINER_ENDPOINT)
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*");

        // 알람  WebSocket 핸들러
        registry.addHandler(getAlarmHandshakeWebSocketHandler(), ALARM_ENDPOINT)
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*");

        log.info("웹소켓 핸들러 등록 완료: 챌린지 채팅 및 트레이너 상담 채팅");

    }

    // 이미지 전송 때문에..........
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean(); // (3)
        container.setMaxTextMessageBufferSize(500000); // (4)
        container.setMaxBinaryMessageBufferSize(500000); // (5)
        return container;
    }

    @Bean
    public WebSocketHandler getChatWebSocketHandler() {
        return new WebSocketHandler();     /* 커스텀 웹소켓 핸들러 */
    }

    // 트레이너용 WebSocket 핸들러
    @Bean
    public WebSocketHandler getTrainerWebSocketHandler() {
        return new WebSocketHandler(); // 다른 핸들러를 사용하거나 같은 핸들러를 재사용할 수 있음
    }

    // 알람
    @Bean
    public AlarmWebSocketHandler getAlarmHandshakeWebSocketHandler()
    {
        return new AlarmWebSocketHandler();
    }


}