package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.UserBookmarkComment;
import dayp308.lebabel.bean.entity.id.UserBookmarkCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentBookmarkRepository extends JpaRepository<UserBookmarkComment, UserBookmarkCommentId> {
    Set<UserBookmarkComment> findByCommentId(Long commentId);
    Set<UserBookmarkComment> findByUserId(Long userId);
    Long countByCommentId(Long commentId);
}
