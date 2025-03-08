package dayp308.chatroom.entity.post;

import java.time.Instant;

public interface PostDTO {
    Long getId();
    PublishUser getPublishUser();
    String getTitle();
    String getContent();
    Category getCategory();
    Byte getStatus();
    Long getViews();
    Instant getLastUpdateTime();
    Instant getCreateTime();
    String getTags();

    interface PublishUser {
        Long getId();
    }

    interface Category {
        Integer getId();
    }
}
