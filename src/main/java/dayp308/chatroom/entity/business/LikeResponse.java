package dayp308.chatroom.entity.business;

import lombok.Value;

@Value
public class LikeResponse {
    private Long count;
    private Boolean liked;
}
