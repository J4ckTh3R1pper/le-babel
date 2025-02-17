package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "tb_user_bookmark_post")
public class UserBookmarkPost {
    @EmbeddedId
    private UserBookmarkPostId id;

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