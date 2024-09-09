package com.sku.fitizen.controller.chetTest;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
// 채팅 메소드에 포함할게 없으면  인터셉턱가 필요없음
@Slf4j  //헨들러보다 먼저 돌아감
public class HttpHandshakeInterceptor implements HandshakeInterceptor
{
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes)
            throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            // 그니깐 웹소캣이 http 파라미터를 못받으니깐  중간에 인터셉터가 가져와서 웹소켓핸들러에게 전달해줌
            // http://localhost/ws/chat?userid=smith 형식으로 웹소켓에 접속할 때, 파라미터를 처리하는 예
            // 아래의 방법을 사용하여 웹사이트에 로그인한 이용자의 ID를 웹소켓핸들러 안으로 전달할 수 있다

            String userid = ((ServletServerHttpRequest) request).getServletRequest().getParameter("userid");
            String roomId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("roomId");

            log.info("인터셉터, userid={}, roomId={}", userid, roomId);

            attributes.put("userid", userid);  // 사용자 아이디 전달
            attributes.put("roomId", roomId);


            //String userid = ((ServletServerHttpRequest) request).getServletRequest().getParameter("userid"); // 서블릿 api에 접근하여 파라미터를 가져오는 . http 가 아니기에  프로토콜이 다르기에
            //log.info("인터셉터, URI={}", request.getURI());
            // log.info("인터셉터, userid={}", userid);


            //attributes.put("userid", userid);  // 웹소켓접속시 사용된 파라미터에서 추출된 userid를 !웹소켓핸들러에게 전달함!
            // 이용자 아이디를 전달하는 과정
            // ServletContext 의 참조를 구할 때는 아래처럼...
            ServletContext ctx = ((ServletServerHttpRequest) request).getServletRequest().getServletContext();
            // ctx 사용...
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {
        // TODO Auto-generated method stub
    }
}

/* json 오브젝트
 *  var obj={};
 *  obj.sender= "smith";
 *  obj.receiver="james";
 *  obj.message="hello World";
 *  obj.room="고딩방";
 *
 *  //json 문자열
 *
 *   "{'key':'value'}"
 *   var jsStr =JSON.stringify(obj) 하면 위에 처럼 바뀜
 *
 *   webSocket.sendMessage(jsStr); json 문자열이 서버로 보낸다
 *   서버에서는 jsonParse 해서 simpleJson
 *
 *   만약 귓속말 할거면 리시버에 나를 포함해서 보내야 내가 보낸 메세지를 알 수 있음
 * */