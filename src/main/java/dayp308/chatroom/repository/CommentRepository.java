package dayp308.chatroom.repository;

import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.repository.projection.CommentProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long>, CustomCommentRepository {
    Slice<PostComment> findAll(Specification<PostComment> specification, Pageable pageable);
    Set<PostComment> findAll(Specification<PostComment> specification);
    long countByPostId(Long postId);

    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("""
        SELECT new dayp308.chatroom.repository.projection.CommentProjection(
            c.id as id,
            c.post.id as postId,
            c.user.id as userId,
            c.commentBody as commentBody,
            c.createTime as createTime,
            c.isDeleted as isDeleted,
            c.parentComment.id as parentCommentId,
            CASE WHEN :userId IS NULL THEN false
                 WHEN l.id IS NOT NULL THEN true ELSE false END
            AS liked,
            COUNT(c.usersLiked) AS likeCount
        ) FROM PostComment c
        LEFT JOIN c.usersLiked as l ON l.id = :userId
        WHERE c.post.id = :postId
        GROUP BY c.id
    """)
    Slice<CommentProjection> findProjectionsByPostId(@Param("postId") long postId, @Param("userId") Long userId, Pageable pageable);


    @Query("""
        SELECT new dayp308.chatroom.repository.projection.CommentProjection(
            c.id as id,
            c.post.id as postId,
            c.user.id as userId,
            c.commentBody as commentBody,
            c.createTime as createTime,
            c.isDeleted as isDeleted,
            c.parentComment.id as parentCommentId,
            CASE WHEN :userId IS NULL THEN false
                 WHEN l.id IS NOT NULL THEN true ELSE false END
            AS liked,
            COUNT(c.usersLiked) AS likeCount
        ) FROM PostComment c
        LEFT JOIN c.usersLiked as l ON l.id = :userId
        WHERE c.id = :id
        GROUP BY c.id
    """)
    CommentProjection findProjectionById(@Param("id") long id, @Param("userId") Long userId);

}
