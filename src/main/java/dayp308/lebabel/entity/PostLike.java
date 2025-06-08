package dayp308.lebabel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.lebabel.entity.id.PostLikeId;
import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_like")
public class PostLike {
    @EmbeddedId
    private PostLikeId id = new PostLikeId();

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}