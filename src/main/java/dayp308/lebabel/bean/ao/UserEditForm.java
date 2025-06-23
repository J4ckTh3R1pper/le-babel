package dayp308.lebabel.bean.ao;

import dayp308.lebabel.enumeration.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {
    private String nickName;
    @Size(max = 144)
    private String introduce;
    private Gender gender;
}
