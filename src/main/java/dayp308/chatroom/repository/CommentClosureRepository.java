package dayp308.chatroom.repository;

import dayp308.chatroom.entity.CommentClosure;
import dayp308.chatroom.entity.CommentClosureId;
import dayp308.chatroom.repository.projection.ClosureProjection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
}
