package dayp308.chatroom.repository.projection;

import java.time.Instant;

public record CommentProjection(
        Long id,
        Long postId,
        Long userId,
        Integer categoryId,
        String commentBody,
        Instant createTime,
        Boolean isDeleted,
        Long parentCommentId,
        Long childCount,
        Boolean liked,
        Long likeCount
) {
}
