package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.CommentClosure;
import dayp308.lebabel.bean.entity.CommentClosureId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
}
