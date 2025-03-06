package dayp308.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public interface PostDTO {
    Long getId();
    PublishUser getPublishUser();
    String getTitle();
    String getContent();
    Category getCategory();
    Byte getStatus();
    Long getViews();
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Instant getLastUpdateTime();
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Instant getCreateTime();
    String getTags();

    interface PublishUser {
        Long getId();
    }

    interface Category {
        Integer getId();
    }
}
