package dayp308.chatroom.entity.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String commentBody;
    private Instant commentCreateTime = Instant.now();
    private Long parentCommentId;
    private boolean isDeleted = false;

    public CommentDTO(Long userId, Long postId, String commentBody) {
        this.userId = userId;
        this.postId = postId;
        this.commentBody = commentBody;
    }
}
