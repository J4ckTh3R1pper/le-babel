package dayp308.chatroom.configuration;

import dayp308.chatroom.deprecated.filter.AuthHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Value("${dayp308.chatroom.viteAddress}")
	private String viteAddress;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(getAuthHandlerInterceptor());
		registration.addPathPatterns("/**").excludePathPatterns("/login", "/check_login", "/auth" ,"/register", "/static/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowCredentials(true)
				.allowedOrigins("http://localhost:5173", viteAddress)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.maxAge(3600);
	}

	@Bean
	public AuthHandlerInterceptor getAuthHandlerInterceptor() {
		return new AuthHandlerInterceptor();
	}

}
