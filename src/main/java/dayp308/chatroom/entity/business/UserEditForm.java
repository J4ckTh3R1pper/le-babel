package dayp308.chatroom.entity.business;

import dayp308.chatroom.entity.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserEditForm {
    private String nickName;
    @Size(max = 32)
    private String introduce;
    private Gender gender;
}
