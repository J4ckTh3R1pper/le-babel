package dayp308.lebabel.entity.business;

import dayp308.lebabel.entity.enums.PostOrderType;

public record PostPageRequest(
        Integer categoryId,
        PostOrderType orderBy,
        String keyword,
        Integer pageNum,
        Integer pageSize,
        boolean ascending,
        boolean visibleOnly) {
}
