package dayp308.lebabel.repository;

import dayp308.lebabel.entity.CommentLike;
import dayp308.lebabel.entity.id.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
    long countByCommentId(Long commentId);

}
