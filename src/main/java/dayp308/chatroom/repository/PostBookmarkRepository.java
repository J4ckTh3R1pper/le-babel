package dayp308.chatroom.repository;

import dayp308.chatroom.entity.UserBookmarkPost;
import dayp308.chatroom.entity.id.UserBookmarkPostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostBookmarkRepository extends JpaRepository<UserBookmarkPost, UserBookmarkPostId> {
    Set<UserBookmarkPost> findByPostId(Long postId);
    Set<UserBookmarkPost> findByUserId(Long userId);
    Long countByPostId(Long postId);
}
