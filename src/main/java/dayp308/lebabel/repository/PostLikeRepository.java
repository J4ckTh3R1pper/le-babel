package dayp308.lebabel.repository;

import dayp308.lebabel.entity.PostLike;
import dayp308.lebabel.entity.id.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    long countByPostId(long postId);
}
