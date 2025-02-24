package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Genders;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "tb_user")
@DynamicInsert
@DynamicUpdate
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

    @ColumnDefault("'/images/avatar/default.jpg'")
    @Generated
    @Column(name = "head_img_url", length = 256, nullable = false)
    private String headImgUrl;

    @ColumnDefault("''")
    @Generated
    @Column(name = "location", nullable = false, length = 4)
    private String location;

    @ColumnDefault("''")
    @Generated
    @Column(name = "introduce", nullable = false, length = 32)
    private String introduce;

    @ColumnDefault("0")
    @Column(name = "user_status", nullable = false)
    private Boolean userStatus = false;

    @ColumnDefault("current_timestamp()")
    @Generated
    @Column(name = "last_login_time", nullable = false)
    private Instant lastLoginTime;

    @ColumnDefault("current_timestamp()")
    @Generated
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("( sha2(concat(`password_md5`, unix_timestamp(), `nick_name`), 256) )")
    @Generated
    @Column(name = "token", unique = true, length = 256)
    private String token;

    @ColumnDefault("'UNKNOWN'")
    @Lob
    @Enumerated(EnumType.STRING)
    @Generated
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
}