package dayp308.chatroom.repository.projection;

import dayp308.chatroom.entity.PostComment;
import org.springframework.stereotype.Component;

@Component
public interface CommentProj {
    PostComment getComment();
}
