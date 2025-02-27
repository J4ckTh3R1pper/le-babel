package dayp308.chatroom.entity.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {
    @NotNull
    private Integer categoryId;
    @NotBlank
    @Size(max = 64)
    private String title;
    @NotBlank
    private String content;
}
