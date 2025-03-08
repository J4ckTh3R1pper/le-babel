package dayp308.chatroom.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.chatroom.entity.post.Post_;
import lombok.Getter;

@Getter
public enum PostOrderType {
        @JsonProperty(Post_.LAST_UPDATE_TIME)
        LAST_UPDATE_TIME(Post_.LAST_UPDATE_TIME),

        @JsonProperty(Post_.CREATE_TIME)
        CREATE_TIME(Post_.CREATE_TIME),

        @JsonProperty(Post_.VIEWS)
        VIEWS(Post_.VIEWS);

        private final String column;

        PostOrderType(String column) {
                this.column = column;
        }

        @Override
        public String toString() {
                return super.toString();
        }
}
