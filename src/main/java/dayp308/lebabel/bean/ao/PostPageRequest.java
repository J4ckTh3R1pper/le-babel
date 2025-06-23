package dayp308.lebabel.bean.ao;

import dayp308.lebabel.enumeration.PostOrderType;

public record PostPageRequest(
        Integer categoryId,
        PostOrderType orderBy,
        String keyword,
        Integer pageNum,
        Integer pageSize,
        boolean ascending,
        boolean visibleOnly) {
}
