package dayp308.chatroom.entity.category;


import java.time.Instant;

public interface CategoryDTO {
    Integer getId();
    String getName();
    Instant getCreateTime();
    String getAvatar();
    String getInfo();
    String getRule();
}