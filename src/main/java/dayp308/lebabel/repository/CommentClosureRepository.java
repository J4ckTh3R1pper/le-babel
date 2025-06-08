package dayp308.lebabel.repository;

import dayp308.lebabel.entity.CommentClosure;
import dayp308.lebabel.entity.CommentClosureId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
}
