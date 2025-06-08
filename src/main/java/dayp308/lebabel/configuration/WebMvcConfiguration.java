package dayp308.lebabel.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Value("${lebabel.vite-host}")
	private String viteAddress;

	@Value("${lebabel.img-path}")
	private String imgPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/api/images/**").addResourceLocations("file:" + imgPath);
		registry.addResourceHandler("/upload/**").addResourceLocations("file:" + imgPath);
		registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowCredentials(true)
				.allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173", viteAddress)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.maxAge(3600);
	}

}
