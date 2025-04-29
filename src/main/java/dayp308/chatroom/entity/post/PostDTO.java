package dayp308.chatroom.entity.post;

import java.time.Instant;

public interface PostDTO {
    Long getId();
    User getUser();
    String getTitle();
    String getContent();
    Category getCategory();
    Byte getStatus();
    Long getViewCount();
    Instant getLastUpdateTime();
    Instant getCreateTime();
    String getTags();

    interface User {
        Long getId();
    }

    interface Category {
        Integer getId();
    }
}
