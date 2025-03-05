package dayp308.chatroom.entity;

import dayp308.chatroom.entity.id.UserBookmarkCommentId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "tb_user_bookmark_comment")
public class UserBookmarkComment {
    @EmbeddedId
    private UserBookmarkCommentId id = new UserBookmarkCommentId();

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment comment;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

}