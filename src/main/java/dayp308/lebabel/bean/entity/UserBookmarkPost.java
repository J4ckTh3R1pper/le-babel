package dayp308.lebabel.bean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.lebabel.bean.entity.id.UserBookmarkPostId;
import dayp308.lebabel.bean.entity.post.Post;
import dayp308.lebabel.bean.entity.user.User;
import jakarta.persistence.*;
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