package dayp308.chatroom.repository;

import dayp308.chatroom.entity.PostLike;
import dayp308.chatroom.entity.id.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    long countByPostId(long postId);
}
