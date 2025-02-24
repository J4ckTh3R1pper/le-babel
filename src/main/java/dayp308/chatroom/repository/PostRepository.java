package dayp308.chatroom.repository;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostCategoryIdAndPostTitleContainingIgnoreCaseOrderByLastUpdateTimeDesc(int categoryId, String keyword, Pageable pageable);
    Page<Post> findAllByPostCategoryIdOrderByLastUpdateTimeDesc(int categoryId, Pageable pageable);

}
