package dayp308.lebabel.bean.ao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginForm(@NotBlank String loginName, @Size(max = 72) String password, Boolean remember) {
}
