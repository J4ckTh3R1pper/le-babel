package dayp308.lebabel.repository;

import dayp308.lebabel.entity.UserBookmarkComment;
import dayp308.lebabel.entity.id.UserBookmarkCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentBookmarkRepository extends JpaRepository<UserBookmarkComment, UserBookmarkCommentId> {
    Set<UserBookmarkComment> findByCommentId(Long commentId);
    Set<UserBookmarkComment> findByUserId(Long userId);
    Long countByCommentId(Long commentId);
}
