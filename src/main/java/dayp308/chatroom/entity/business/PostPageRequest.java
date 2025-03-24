package dayp308.chatroom.entity.business;

import dayp308.chatroom.entity.enums.PostOrderType;

public record PostPageRequest(
        Integer categoryId,
        PostOrderType orderBy,
        String keyword,
        Integer pageNum,
        Integer pageSize,
        boolean ascending,
        boolean visibleOnly) {
}
