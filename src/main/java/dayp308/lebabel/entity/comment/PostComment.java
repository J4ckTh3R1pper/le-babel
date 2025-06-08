package dayp308.lebabel.entity.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tb_post_comment")
@DynamicInsert
@DynamicUpdate
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_user_id", nullable = false)
    private User user;

    @ColumnDefault("''")
    @Column(name = "comment_body", nullable = false, length = 512)
    private String commentBody;

    @ColumnDefault("current_timestamp()")
    @Column(name = "comment_create_time", nullable = false)
    private Instant createTime = Instant.now();

    @ColumnDefault("0")
    @Column(name = "is_deleted", length = 1)
    private Boolean isDeleted = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "bookmarkedComments")
    private Set<User> bookmarkedUsers = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likedComments")
    private Set<User> usersLiked = new LinkedHashSet<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "comment_closure",
        joinColumns = @JoinColumn(name = "descendant"),
        inverseJoinColumns = @JoinColumn(name = "ancestor")
    )
    private Set<PostComment> ancestors = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "ancestors")
    private Set<PostComment> descendants = new LinkedHashSet<>();
    

    // @JsonManagedReference
    // @OneToMany(mappedBy = "parentComment")
    // private Set<PostComment> subComments = new LinkedHashSet<>();


}