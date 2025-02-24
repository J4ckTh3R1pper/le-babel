package dayp308.chatroom.filter;

import dayp308.chatroom.deprecated.model.User;
import dayp308.chatroom.deprecated.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.lang.Nullable;

public class AuthHandlerInterceptor implements HandlerInterceptor {

	private UserService userService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest req, @NonNull HttpServletResponse resp, @NonNull Object handler) throws Exception {
        try {
			boolean bl = false;
            HttpSession session = req.getSession();
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
			String token = null;
			if ( session.getAttribute("user") instanceof User )
				token = ( (User) session.getAttribute("user") ).getToken();

			User loginUser = userService.getUserByToken(token);

            if (loginUser != null)
				bl = true;
            else {
				Cookie[] cookie = req.getCookies();
				if (cookie != null) {
					for (Cookie c : cookie) {
						if (c.getName().equals("token")) {
							token = c.getValue();
							break;
						}
					}
				}
				if ( token != null && !token.isEmpty()) {
					loginUser = userService.getUserByToken(token);
//				    System.out.println(user);
				}

				if ( loginUser != null) {
					bl = true;
				}

			}
			if (bl) {
				loginUser.setPassword("");
				session.setAttribute("user", loginUser);
//				System.out.println(session.getAttribute("user"));
			} else resp.sendRedirect("/login");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, @Nullable ModelAndView view) throws Exception {
    }
}
