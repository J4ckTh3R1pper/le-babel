package dayp308.lebabel.bean.entity.projection;

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