package dayp308.chatroom.entity;

import dayp308.chatroom.entity.id.UserBookmarkPostId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "tb_user_bookmark_post")
public class UserBookmarkPost {
    @EmbeddedId
    private UserBookmarkPostId id = new UserBookmarkPostId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

}