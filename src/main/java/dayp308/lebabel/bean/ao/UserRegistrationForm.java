package dayp308.lebabel.bean.ao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
