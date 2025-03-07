package dayp308.chatroom.entity.business;

import lombok.Value;

public record CommentPageRequest(Long postId, Integer pageNum, Integer pageSize, boolean ascending,
                                 boolean visibleOnly) {
}
