package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.PostLike;
import dayp308.lebabel.bean.entity.id.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    long countByPostId(long postId);
}
