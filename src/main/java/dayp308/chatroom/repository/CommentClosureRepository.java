package dayp308.chatroom.repository;

import dayp308.chatroom.entity.CommentClosure;
import dayp308.chatroom.entity.CommentClosureId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
}
