package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "tb_post_comment")
@DynamicInsert
@DynamicUpdate
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comment_user_id", nullable = false)
    private User commentUser;

    @ColumnDefault("''")
    @Column(name = "comment_body", nullable = false, length = 512)
    private String commentBody;

    @ColumnDefault("current_timestamp()")
    @Column(name = "comment_create_time", nullable = false)
    private Instant commentCreateTime = Instant.now();

    @ColumnDefault("0")
    @Column(name = "is_deleted", length = 1)
    private boolean isDeleted = false;

    @ManyToMany(mappedBy = "bookmarkedComments")
    private List<User> bookmarkedUsers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

//    @OneToMany(mappedBy = "parentComment")
//    private List<PostComment> subComments;


}