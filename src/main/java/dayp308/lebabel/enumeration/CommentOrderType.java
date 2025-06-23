package dayp308.lebabel.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.lebabel.bean.entity.comment.PostComment_;

public enum CommentOrderType {
    @JsonProperty(PostComment_.CREATE_TIME)
    CREATE_TIME,
    @JsonProperty("likeCount")
    LIKE_COUNT,
    @JsonProperty("subCommentCount")
    SUB_COMMENT_COUNT,
}
