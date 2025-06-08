package dayp308.lebabel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("dayp308.lebabel.repository")
public class ChatSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatSpringbootApplication.class, args);
    }

}
