package dayp308.chatroom.entity.business;

import dayp308.chatroom.annotation.PostIdExists;
import dayp308.chatroom.annotation.UserTokenExists;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreationForm implements Serializable {

    @NotBlank
    private String commentBody;
    @NotNull
    private Long postId;

    private Long parentCommentId;
    @NotNull
    @Size(min = 32, max = 32)
    private String token;
}
