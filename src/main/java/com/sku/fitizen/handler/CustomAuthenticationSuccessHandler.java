package com.sku.fitizen.handler;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.role;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String id = authentication.getName();
        User user = userService.findUserByUsername(id);
        request.getSession().setAttribute("user", user);

        if (user != null) {
            request.getSession().setAttribute("userId", user.getId()); // user 객체의 ID 속성 사용
            log.info("UserId set in session: " + user.getId());
        } else {
            log.warn("User object is null, userId not set in session.");
        }


        String prevPage = (String) request.getSession().getAttribute("prevPage");
        log.info("Previous page: " + prevPage);
        // URL에서 경로 부분만 추출
        if (prevPage != null) {
            try {
                prevPage = new URL(prevPage).getPath();
            } catch (MalformedURLException e) {
                log.error("Malformed URL in prevPage", e);
                prevPage = "/";
            }
        }

        if (prevPage != null && !prevPage.equals("/register/add")) {
            request.getSession().removeAttribute("prevPage");
            getRedirectStrategy().sendRedirect(request, response, prevPage);
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
