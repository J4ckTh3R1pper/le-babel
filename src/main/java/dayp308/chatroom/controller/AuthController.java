package dayp308.chatroom.controller;

import dayp308.chatroom.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/check_login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @Nullable @RequestParam("remember") String remember,
                                HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        System.out.println("remember: " + remember);
        try {
            String token = userService.login(username, password);
            session.setAttribute("user", token);
            if (remember != null) {
                Arrays.stream(req.getCookies())
                        .filter(cookie -> cookie.getName().contains("token"))
                        .forEach(c -> {
                    c.setMaxAge(0);
                    resp.addCookie(c);
                });
                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                cookie.setMaxAge(604800); // cookie保存7天时间
                resp.addCookie(cookie);
            }
            return ResponseEntity.ok("successfully logged in");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}
