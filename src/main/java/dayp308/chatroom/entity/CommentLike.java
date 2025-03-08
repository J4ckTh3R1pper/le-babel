package dayp308.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.id.CommentLikeId;
import dayp308.chatroom.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "comment_like")
public class CommentLike {
    @EmbeddedId
    private CommentLikeId id = new CommentLikeId();

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("commentId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment comment;

    public CommentLike(PostComment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}