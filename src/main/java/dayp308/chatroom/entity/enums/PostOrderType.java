package dayp308.chatroom.entity.enums;

import lombok.Getter;

@Getter
public enum PostOrderType {
        LAST_UPDATE_TIME(1, "lastUpdateTime"),
        CREATE_TIME(2, "createTime"),
        VIEWS(3, "views");

        private final int value;
        private final String column;

        PostOrderType(int value, String column) {
                this.value = value;
                this.column = column;
        }

}
