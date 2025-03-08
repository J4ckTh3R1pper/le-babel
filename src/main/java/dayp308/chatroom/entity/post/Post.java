package dayp308.chatroom.entity.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publish_user_id", nullable = false)
    private User publishUser;

    @Column(name = "post_title", nullable = false, length = 64)
    private String title;

    @Lob
    @Column(name = "post_content", nullable = false)
    private String content;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_category_id", nullable = false)
    private PostCategory category;

    @ColumnDefault("0")
    @Column(name = "post_status", nullable = false)
    private Byte status = 1;

    @ColumnDefault("0")
    @Column(name = "post_views", nullable = false)
    private Long views = 0L;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_update_time", nullable = false)
    private Instant lastUpdateTime = Instant.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime = Instant.now();

    @ColumnDefault("'[]'")
    @Lob
    @Column(name = "post_tags", nullable = false)
    private String tags = "[]";

    @JsonIgnore
    @ManyToMany(mappedBy = "bookmarkedPosts")
    private Set<User> bookmarkedUsers = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private Set<PostComment> comments = new LinkedHashSet<>();

    @JsonManagedReference

    @ManyToMany(mappedBy = "likedPosts", fetch = FetchType.LAZY)
    private Set<User> usersLiked = new LinkedHashSet<>();
}