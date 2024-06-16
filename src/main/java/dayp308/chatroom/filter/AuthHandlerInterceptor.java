package dayp308.chatroom.filter;

import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import java.io.IOException;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        try {
            HttpSession session = req.getSession();
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");

            Object user = session.getAttribute("user");

            if (user != null) {
                return true;
            }
            resp.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
