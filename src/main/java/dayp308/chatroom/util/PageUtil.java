package dayp308.chatroom.util;

import dayp308.chatroom.entity.view.LikeablePagedResponse;
import dayp308.chatroom.entity.view.PagedResponse;
import org.springframework.data.domain.Slice;

import java.util.List;

public class PageUtil {
    public static <T> PagedResponse<T> sliceToResponse(Slice slice, List<T> content) {
        PagedResponse<T> resp = new PagedResponse<>();
        resp.setContent(content);
        resp.setHasNext(slice.hasNext());
        resp.setHasPrevious(slice.hasPrevious());
        resp.setPageNum(slice.getPageable().getPageNumber());
        resp.setPageSize(slice.getPageable().getPageSize());
        return resp;
    }

    public static <T> LikeablePagedResponse<T> sliceToLikeResponse(Slice slice, List<T> content) {
        LikeablePagedResponse<T> resp = new LikeablePagedResponse<>();
        resp.setContent(content);
        resp.setHasNext(slice.hasNext());
        resp.setHasPrevious(slice.hasPrevious());
        resp.setPageNum(slice.getPageable().getPageNumber());
        resp.setPageSize(slice.getPageable().getPageSize());
        return resp;
    }
}
