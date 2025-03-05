package dayp308.chatroom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
public class PersistentToken {
    private Long userId;
    private String token;

}
