package dayp308.lebabel.bean.entity.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.bean.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Indexed
@Table(name = "tb_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericField(projectable = Projectable.YES)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publish_user_id", nullable = false)
    private User user;

    @Column(name = "post_title", nullable = false, length = 192)
    @FullTextField(name = "title", projectable = Projectable.YES)
    private String title;

    @Lob
    @Column(name = "post_content", nullable = false)
    @FullTextField(name = "content")
    // @GenericField(projectable = Projectable.YES)
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
    private Long viewCount = 0L;

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
    @ManyToMany( mappedBy = "bookmarkedPosts")
    private Set<User> bookmarkedUsers = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private Set<PostComment> comments = new LinkedHashSet<>();

    @JsonManagedReference
    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> usersLiked = new LinkedHashSet<>();
}