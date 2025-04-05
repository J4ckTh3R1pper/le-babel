package dayp308.chatroom.entity.view.comment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Component
public class CommentView {
    protected Long id;
    protected Long postId;
    protected Long userId;
    protected String commentBody;
    protected Instant createTime;
    protected Long likeCount;
    protected Boolean liked;

    protected CommentView(){}

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
