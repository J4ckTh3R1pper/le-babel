package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
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

    @ColumnDefault("'/images/avatar/default.jpg'")
    @Column(name = "head_img_url", length = 256, nullable = false)
    private String headImgUrl = "/images/avatar/default.jpg";

    @ColumnDefault("''")
    @Column(name = "location", nullable = false, length = 4)
    private String location = "";

    @ColumnDefault("''")
    @Column(name = "introduce", nullable = false, length = 32)
    private String introduce = "";

    @ColumnDefault("0")
    @Column(name = "user_locked", nullable = false)
    private Boolean userLocked = false;

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

//    @OneToMany(mappedBy = "user")
//    private Set<CategoryMember> categoriesMember = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "sender")
//    private Set<ChatMessagePrivate> chatMessagesPrivate = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "target")
//    private Set<ChatMessagePrivate> receivedChatMessagesPrivate = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "sender")
//    private Set<ChatMessagePublic> chatMessagesPublic = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "publishUser")
//    private Set<Post> posts = new LinkedHashSet<>();

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

//    @OneToMany(mappedBy = "commentUser")
//    private Set<PostComment> comments = new LinkedHashSet<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.userLocked;
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