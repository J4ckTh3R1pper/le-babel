package dayp308.chatroom.entity.category;

import java.time.Instant;

public record CategoryProjection(
    int id,
    String name,
    Instant createTime,
    String avatar,
    String info,
    String rule,
    long subscriberCount
) {
} 