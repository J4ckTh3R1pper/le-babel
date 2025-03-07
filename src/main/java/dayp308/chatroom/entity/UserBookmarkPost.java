package dayp308.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.chatroom.entity.id.UserBookmarkPostId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tb_user_bookmark_post")
public class UserBookmarkPost {
    @EmbeddedId
    private UserBookmarkPostId id = new UserBookmarkPostId();

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

}