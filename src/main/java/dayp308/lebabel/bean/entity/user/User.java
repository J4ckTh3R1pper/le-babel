package dayp308.lebabel.bean.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dayp308.lebabel.bean.entity.ChatMessagePrivate;
import dayp308.lebabel.bean.entity.ChatMessagePublic;
import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.enumeration.Gender;
import dayp308.lebabel.bean.entity.member.CategoryMember;
import dayp308.lebabel.bean.entity.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tb_user")
@DynamicInsert
@DynamicUpdate
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @ColumnDefault("''")
    @Column(name = "login_name", nullable = false, length = 32, unique = true)
    private String loginName;

    @ColumnDefault("''")
    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @ColumnDefault("''")
    @Column(name = "nick_name", nullable = false, length = 8, unique = true)
    private String nickName;

    @ColumnDefault("")
    @Column(name = "head_img_url", length = 256, nullable = false)
    private String headImgUrl;

    @ColumnDefault("''")
    @Column(name = "location", nullable = false, length = 4)
    private String location = "";

    @ColumnDefault("''")
    @Column(name = "introduce", nullable = false, length = 144)
    private String introduce = "";

    @ColumnDefault("0")
    @Column(name = "user_locked", nullable = false)
    private Boolean locked = false;

    @ColumnDefault("current_timestamp()")
    @Column(name = "last_login_time", nullable = false)
    private Instant lastLoginTime = Instant.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime = Instant.now();

    @ColumnDefault("'UNKNOWN'")
    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<CategoryMember> categoriesMember = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private Set<ChatMessagePrivate> chatMessagesPrivate = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "target")
    private Set<ChatMessagePrivate> receivedChatMessagesPrivate = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private Set<ChatMessagePublic> chatMessagesPublic = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tb_user_bookmark_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> bookmarkedPosts = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tb_user_bookmark_comment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<PostComment> bookmarkedComments = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "post_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "comment_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<PostComment> likedComments = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_user_id")
    )
    private Set<User> subscriptions = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<PostComment> comments = new LinkedHashSet<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new GrantedAuthority[]{new SimpleGrantedAuthority("ROLE_USER")});
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}