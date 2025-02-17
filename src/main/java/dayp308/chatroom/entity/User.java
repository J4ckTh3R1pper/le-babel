package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Genders;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @ColumnDefault("''")
    @Column(name = "login_name", nullable = false, length = 32, unique = true)
    private String loginName;

    @ColumnDefault("''")
    @Column(name = "password_md5", nullable = false, length = 32)
    private String passwordMd5;

    @ColumnDefault("''")
    @Column(name = "nick_name", nullable = false, length = 8, unique = true)
    private String nickName;

    @ColumnDefault("''")
    @Column(name = "head_img_url", nullable = false, length = 256)
    private String headImgUrl;

    @ColumnDefault("''")
    @Column(name = "location", nullable = false, length = 4)
    private String location;

    @ColumnDefault("''")
    @Column(name = "introduce", nullable = false, length = 32)
    private String introduce;

    @ColumnDefault("0")
    @Column(name = "user_status", nullable = false)
    private Boolean userStatus = false;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_login_time", nullable = false)
    private Instant lastLoginTime;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("'unknown'")
    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Genders gender;

    @OneToMany(mappedBy = "user")
    private Set<CategoryMember> categoriesMember = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<ChatMessagePrivate> chatMessagesPrivate = new LinkedHashSet<>();

    @OneToMany(mappedBy = "target")
    private Set<ChatMessagePrivate> receivedChatMessagesPrivate = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<ChatMessagePublic> chatMessagesPublic = new LinkedHashSet<>();

    @OneToMany(mappedBy = "publishUser")
    private Set<Post> posts = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tb_user_bookmark_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> bookmarkedPosts = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tb_user_bookmark_comment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<PostComment> bookmarkedComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "commentUser")
    private Set<PostComment> comments = new LinkedHashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", passwordMd5='" + passwordMd5 + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", location='" + location + '\'' +
                ", introduce='" + introduce + '\'' +
                ", userStatus=" + userStatus +
                ", lastLoginTime=" + lastLoginTime +
                ", createTime=" + createTime +
                ", gender=" + gender +
                ", categoriesMember=" + categoriesMember +
                ", chatMessagesPrivate=" + chatMessagesPrivate +
                ", receivedChatMessagesPrivate=" + receivedChatMessagesPrivate +
                ", chatMessagesPublic=" + chatMessagesPublic +
                ", posts=" + posts +
                ", bookmarkedPosts=" + bookmarkedPosts +
                ", bookmarkedComments=" + bookmarkedComments +
                ", comments=" + comments +
                '}';
    }
}