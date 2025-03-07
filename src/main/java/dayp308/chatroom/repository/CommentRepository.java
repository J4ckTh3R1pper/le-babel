package dayp308.chatroom.repository;

import dayp308.chatroom.entity.PostComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long> {
    Slice<PostComment> findAll(Specification<PostComment> specification, Pageable pageable);
    Set<PostComment> findAll(Specification<PostComment> specification);
    long countByPostId(Long postId);

    <T> Optional<T> findById(Long id, Class<T> type);

}
