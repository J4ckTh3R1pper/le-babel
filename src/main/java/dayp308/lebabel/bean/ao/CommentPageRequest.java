package dayp308.lebabel.bean.ao;

public record CommentPageRequest(Long postId, Integer pageNum, Integer pageSize, boolean ascending,
                                 boolean visibleOnly) {
}
