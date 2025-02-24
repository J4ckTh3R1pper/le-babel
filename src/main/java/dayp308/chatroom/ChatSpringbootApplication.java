package dayp308.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("dayp308.chatroom.repository")
public class ChatSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatSpringbootApplication.class, args);
    }

}
