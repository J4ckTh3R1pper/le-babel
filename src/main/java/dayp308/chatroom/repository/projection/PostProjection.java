package dayp308.chatroom.repository.projection;

import java.time.Instant;

public record PostProjection(
        Long id,
        Long userId,
        String title,
        String content,
        Integer categoryId,
        Instant createTime,
        Instant lastUpdateTime,
        String tags,
        Long viewCount,
        Boolean liked,
        Long likeCount,
        Long commentCount
) {
}
