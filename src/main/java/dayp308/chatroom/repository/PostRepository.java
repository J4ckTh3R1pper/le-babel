package dayp308.chatroom.repository;

import dayp308.chatroom.entity.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findByCategoryIdAndTitleContainingIgnoreCaseOrderByLastUpdateTimeDesc(int categoryId, String keyword, Pageable pageable);
    Slice<Post> findByCategoryIdAndTitleContainingIgnoreCaseOrderByLastUpdateTimeAsc(int categoryId, String keyword, Pageable pageable);
    Slice<Post> findAllByCategoryIdOrderByLastUpdateTimeDesc(int categoryId, Pageable pageable);

    Slice<Post> findAll(Specification<Post> spec, Pageable pageable);

    <T> Optional<T> findById(long id, Class<T> clazz);
}
