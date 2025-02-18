<script setup>
import HelloWorld from './components/HelloWorld.vue'
import Markdown from "./components/Markdown.vue";
import {ref, computed} from 'vue'
const code = ref(`# 123\n\`\`\`java
package dayp308.chatroom.filter;

import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.lang.Nullable;

import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import java.io.IOException;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

	@Autowired
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
				if ( token == null || token.equals("") ) {
					bl = false;
				} else {
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
\`\`\`
`)


</script>

<script>
</script>

<template>
  <div>
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo" alt="Vite logo" />
    </a>
    <a href="https://vuejs.org/" target="_blank">
      <img src="./assets/vue.svg" class="logo vue" alt="Vue logo" />
    </a>
  </div>
<!--  <HelloWorld msg="Vite + Vue" />-->
  <Markdown :md-text="code"></Markdown>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
