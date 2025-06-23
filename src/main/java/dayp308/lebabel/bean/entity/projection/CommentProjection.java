package dayp308.lebabel.bean.entity.projection;

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
