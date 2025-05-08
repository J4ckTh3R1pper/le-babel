package dayp308.chatroom.entity.business;

import dayp308.chatroom.entity.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserEditForm {
    private String nickName;
    @Size(max = 144)
    private String introduce;
    private Gender gender;
}
