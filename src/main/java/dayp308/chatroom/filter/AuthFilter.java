package dayp308.chatroom.filter;

import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.IUserService;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

//@WebFilter(filterName = "auth")
// @Configuration
public class AuthFilter implements Filter {

    // @Autowired
    private UserService userService;

    @Bean
    public FilterRegistrationBean<AuthFilter> setBean() {
        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter( new AuthFilter() );
        bean.addUrlPatterns("/app");
        return bean;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        //获取servlet上下文
        ServletContext sc = req.getSession().getServletContext();
        //获取spring容器
        AbstractApplicationContext appContext = (AbstractApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        if (appContext != null && appContext.getBean("userService") != null && userService != null) {
            userService = (UserService) appContext.getBean("userService");
        }
        String token = null;
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        boolean bl = (req.getRequestURI().equals("/login") || req.getRequestURI().equals("/register"));
        if (session.getAttribute("token") != null)
            token = session.getAttribute("token").toString();
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
        }
        if (!bl) {
            if (token == null) {
                resp.sendRedirect("/login");
            } else {
                User user = userService.getUserByToken(token);
                user.setPassword("");
//            System.out.println(user);
                req.getSession().setAttribute("user", user);
            }
        } else if (token != null) {
            resp.sendRedirect("/app");
            filterChain.doFilter(req, resp);
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
