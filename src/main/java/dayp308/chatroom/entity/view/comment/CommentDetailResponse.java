package dayp308.chatroom.entity.view.comment;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class CommentDetailResponse {
    CommentDetailedView view;
    Set<Long> likedIds;
}
