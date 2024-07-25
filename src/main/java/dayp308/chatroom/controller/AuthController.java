package dayp308.chatroom.controller;

import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/check_login", method = RequestMethod.POST)
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @Nullable @RequestParam("remember") String remember,
                      HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        System.out.println("remember: " + remember);
        try {
            User user = userService.getUserLogin(username, password);
            if (user != null) {
                session.setAttribute("user", user);
                if (remember != null) {
                    Cookie cookie = new Cookie("token", user.getToken());
                    cookie.setPath("/");
                    cookie.setMaxAge(24 * 60 * 60 * 7);
                    resp.addCookie(cookie);
                }
                resp.sendRedirect("/chat?serverId=1");
            }
            else resp.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
