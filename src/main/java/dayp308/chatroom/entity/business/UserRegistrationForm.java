package dayp308.chatroom.entity.business;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class UserRegistrationForm {

    @Email
    @NotNull
    private String loginName;
    @NotBlank
    private String nickName;

    @Size(min = 8, max = 72)
    private String password;
    private String uuid;
    private String captcha;
}
