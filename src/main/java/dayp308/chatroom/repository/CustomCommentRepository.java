package dayp308.chatroom.repository;

import dayp308.chatroom.entity.enums.CommentOrderType;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.repository.projection.CommentData;
import dayp308.chatroom.repository.projection.CommentProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CustomCommentRepository {
    CommentProjection findProjById(long id, @Nullable User user, boolean filterDeleted);
    Slice<CommentProjection> findAllProjByPostId(long postId, @Nullable User user, Pageable pageable, boolean filterDeleted);
    List<CommentProjection> findAllProjByCommentId(long commentId, @Nullable User user, int limit, boolean filterDeleted);

    List<CommentProjection> findClosureListByCommentId(long commentId, User user, int limit, boolean filterDeleted);
    CommentData getCommentDataById(long commentId, User user);
}
