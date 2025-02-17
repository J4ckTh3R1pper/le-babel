package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tb_post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @ColumnDefault("0")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comment_user_id", nullable = false)
    private User commentUser;

    @ColumnDefault("''")
    @Column(name = "comment_body", nullable = false, length = 200)
    private String commentBody;

    @ColumnDefault("current_timestamp()")
    @Column(name = "comment_create_time", nullable = false)
    private Instant commentCreateTime;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Byte isDeleted;

    @ManyToMany(mappedBy = "bookmarkedComments")
    private List<User> bookmarkedUsers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<PostComment> subComments;


}