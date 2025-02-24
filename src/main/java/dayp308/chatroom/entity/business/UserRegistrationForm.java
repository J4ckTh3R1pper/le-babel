package dayp308.chatroom.entity.business;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegistrationForm {

    @Email
    private String loginName;
    @NotBlank
    private String nickName;

    @Size(min = 8, max = 64)
    private String password;
}
