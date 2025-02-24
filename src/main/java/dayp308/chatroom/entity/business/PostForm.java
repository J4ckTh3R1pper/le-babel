package dayp308.chatroom.entity.business;

import dayp308.chatroom.annotation.CategoryIdExists;
import dayp308.chatroom.annotation.UserTokenExists;
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
    @Size(min = 32, max = 32)
    private String token;
    @NotNull
    private Integer categoryId;
    @NotBlank
    @Size(max = 64)
    private String title;
    @NotBlank
    private String content;
}
