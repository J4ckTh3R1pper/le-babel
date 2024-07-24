package dayp308.chatroom.configuration;

import dayp308.chatroom.filter.AuthHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(getAuthHandlerInterceptor());
		registration.addPathPatterns("/**").excludePathPatterns("/login", "/check_login", "/auth" ,"/register", "/static/**");
	}

	@Bean
	public AuthHandlerInterceptor getAuthHandlerInterceptor() {
		return new AuthHandlerInterceptor();
	}

}
