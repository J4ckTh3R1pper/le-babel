package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.comment.CommentClosure;
import dayp308.lebabel.bean.entity.id.CommentClosureId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
}
