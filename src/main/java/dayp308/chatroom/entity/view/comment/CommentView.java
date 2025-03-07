package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.entity.view.UserBriefView;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Data
@Component
public class CommentView {
    protected Long id;
    protected Long postId;
    protected UserBriefView userBriefView;
    protected String commentBody;
    protected Instant createTime;
    protected Long parentCommentId;
    protected Integer likes;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommentView that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public CommentBriefView getBriefView() {
        CommentBriefView target = new CommentBriefView();
        BeanUtils.copyProperties(this, target);
        return target;
    }

    public CommentDetailedView getDetailedView() {
        CommentDetailedView target = new CommentDetailedView();
        BeanUtils.copyProperties(this, target);
        return target;
    }

}
