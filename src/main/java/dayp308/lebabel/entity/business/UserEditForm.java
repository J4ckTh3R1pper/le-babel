package dayp308.lebabel.entity.business;

import dayp308.lebabel.entity.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {
    private String nickName;
    @Size(max = 144)
    private String introduce;
    private Gender gender;
}
