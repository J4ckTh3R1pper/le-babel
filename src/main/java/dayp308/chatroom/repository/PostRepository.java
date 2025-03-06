package dayp308.chatroom.repository;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.PostDTO;
import dayp308.chatroom.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findByCategoryIdAndTitleContainingIgnoreCaseOrderByLastUpdateTimeDesc(int categoryId, String keyword, Pageable pageable);
    Slice<Post> findAllByCategoryIdOrderByLastUpdateTimeDesc(int categoryId, Pageable pageable);

    <T> T findById(long id, Class<T> clazz);
}
