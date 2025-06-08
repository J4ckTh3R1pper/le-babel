package dayp308.lebabel.entity.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
