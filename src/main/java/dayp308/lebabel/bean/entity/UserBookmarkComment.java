package dayp308.lebabel.bean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.bean.entity.id.UserBookmarkCommentId;
import dayp308.lebabel.bean.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Entity
@Getter
@Setter
@Table(name = "tb_user_bookmark_comment")
public class UserBookmarkComment {
    @EmbeddedId
    private UserBookmarkCommentId id = new UserBookmarkCommentId();

    @MapsId("commentId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment comment;

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

}