package dayp308.chatroom.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.chatroom.entity.comment.PostComment_;

public enum CommentOrderType {
    @JsonProperty(PostComment_.CREATE_TIME)
    CREATE_TIME,
    @JsonProperty("likeCount")
    LIKE_COUNT,
    @JsonProperty("subCommentCount")
    SUB_COMMENT_COUNT,
}
