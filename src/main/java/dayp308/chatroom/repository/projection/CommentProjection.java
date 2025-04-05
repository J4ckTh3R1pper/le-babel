package dayp308.chatroom.repository.projection;

import lombok.Value;

import java.time.Instant;

public record CommentProjection(
        Long id,
        Long postId,
        Long userId,
        String commentBody,
        Instant createTime,
        Boolean isDeleted,
        Long parentCommentId,
        Boolean liked,
        Long likeCount
) {
}
