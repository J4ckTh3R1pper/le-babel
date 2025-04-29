package dayp308.chatroom.repository.listener;

import org.springframework.context.ApplicationEvent;

import dayp308.chatroom.entity.comment.PostComment;
import lombok.Getter;

@Getter
public class CommentCreateEvent extends ApplicationEvent {
    private Long ancestorId;
    private Long id;

    public CommentCreateEvent(PostComment source) {
        super(source);
        //TODO Auto-generated constructor stub
    }

    @Override
    public PostComment getSource() {
        return (PostComment) this.source;
    }
}