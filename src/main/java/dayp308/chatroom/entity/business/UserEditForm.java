package dayp308.chatroom.entity.business;

import dayp308.chatroom.annotation.UserTokenExists;
import dayp308.chatroom.entity.enums.Genders;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserEditForm {
    @UserTokenExists
    private String token;
    private String nickName;
    @Size(max = 32)
    private String introduce;
    private Genders gender;
}
