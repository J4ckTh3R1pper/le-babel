package dayp308.lebabel.entity.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.lebabel.entity.post.Post_;
import lombok.Getter;

@Getter
public enum PostOrderType {
        @JsonEnumDefaultValue
        @JsonProperty(Post_.LAST_UPDATE_TIME)
        LAST_UPDATE_TIME,
        @JsonProperty(Post_.CREATE_TIME)
        CREATE_TIME,
        @JsonProperty("likeCount")
        LIKE_COUNT,
        @JsonProperty(Post_.VIEW_COUNT)
        VIEW_COUNT,
        @JsonProperty("commentCount")
        COMMENT_COUNT
}
