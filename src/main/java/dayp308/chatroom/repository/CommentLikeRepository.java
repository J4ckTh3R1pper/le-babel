package dayp308.chatroom.repository;

import dayp308.chatroom.entity.CommentLike;
import dayp308.chatroom.entity.id.CommentLikeId;
import dayp308.chatroom.repository.projection.CommentProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
    long countByCommentId(Long commentId);

    Set<CommentProj> findBy(CommentLikeId id);
}
