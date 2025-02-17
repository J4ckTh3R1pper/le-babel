package dayp308.chatroom.entity;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "publish_user_id", nullable = false)
    private dayp308.chatroom.entity.User publishUser;

    @ColumnDefault("''")
    @Column(name = "post_title", nullable = false, length = 64)
    private String postTitle;

    @Lob
    @Column(name = "post_content", nullable = false)
    private String postContent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "post_category_id", nullable = false)
    private PostCategory postCategory;

    @ColumnDefault("1")
    @Column(name = "post_status", nullable = false)
    private Byte postStatus;

    @ColumnDefault("0")
    @Column(name = "post_views", nullable = false)
    private Long postViews;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_update_time", nullable = false)
    private Instant lastUpdateTime;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("'[]'")
    @Lob
    @Column(name = "post_tags", nullable = false)
    private String postTags;

    @ManyToMany(mappedBy = "bookmarkedPosts")
    private Set<User> bookmarkedUsers = new LinkedHashSet<>();

    @OneToMany
    private Set<PostComment> postComments = new LinkedHashSet<>();

}