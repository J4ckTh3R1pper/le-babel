package dayp308.lebabel.entity.business;

public record CommentPageRequest(Long postId, Integer pageNum, Integer pageSize, boolean ascending,
                                 boolean visibleOnly) {
}
