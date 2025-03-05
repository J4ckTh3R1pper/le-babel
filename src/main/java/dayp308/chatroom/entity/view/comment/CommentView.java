package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.entity.view.Likeable;
import dayp308.chatroom.entity.view.UserBriefView;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Data
@Component
public class CommentView implements Likeable {
    protected Long commentId;
    protected Long postId;
    protected UserBriefView userBriefView;
    protected String commentBody;
    protected Instant commentCreateTime;
    protected Long parentCommentId;
    protected Long likes;
    protected boolean liked = false;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommentView that)) return false;
        return Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentId);
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

    @Override
    public long getId() {
        return this.commentId;
    }

    @Override
    public void setId(long id) {
        this.commentId = id;
    }

    @Override
    public boolean getLike() {
        return this.liked;
    }

    @Override
    public void setLike(boolean like) {
        this.liked = like;
    }
}
