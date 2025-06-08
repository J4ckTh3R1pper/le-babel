package dayp308.lebabel.repository;

import dayp308.lebabel.entity.UserBookmarkPost;
import dayp308.lebabel.entity.id.UserBookmarkPostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostBookmarkRepository extends JpaRepository<UserBookmarkPost, UserBookmarkPostId> {
    Set<UserBookmarkPost> findByPostId(Long postId);
    Set<UserBookmarkPost> findByUserId(Long userId);
    Long countByPostId(Long postId);
}
