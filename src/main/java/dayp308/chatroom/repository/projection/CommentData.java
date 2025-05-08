package dayp308.chatroom.repository.projection;

import lombok.Value;

@Value
public class CommentData {
    private Long childCount;
    private Boolean liked;
    private Long likeCount;
}
