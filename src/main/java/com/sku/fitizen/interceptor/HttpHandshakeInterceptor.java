package com.sku.fitizen.interceptor;
import com.sku.fitizen.domain.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

// 채팅 메소드에 포함할게 없으면  인터셉턱가 필요없음
@Slf4j  //헨들러보다 먼저 돌아감
public class HttpHandshakeInterceptor implements HandshakeInterceptor
{
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request, ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes)
            throws Exception {
        if (request instanceof ServletServerHttpRequest) {

            User user =(User) ((ServletServerHttpRequest) request).getServletRequest().getSession().getAttribute("user");
            String roomId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("roomId");
            String consultId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("consultId");
            Principal userPrincipal = request.getPrincipal();
            attributes.put("user", user);
            attributes.put("a", userPrincipal); // WebSocket 세션에 인증 사용자 추가


            log.info("인터셉터, userId={}, roomId={} consultId={}", user.getId(), roomId , consultId);

            attributes.put("user", user);  // 사용자 정보 전달
            attributes.put("roomId", roomId);
            attributes.put("consultId",consultId);


        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {
        // TODO Auto-generated method stub
    }
}
