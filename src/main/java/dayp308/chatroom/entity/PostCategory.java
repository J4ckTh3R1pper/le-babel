package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.Objects;

@Setter
@Getter
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_post_category")
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 16, unique = true)
    private String categoryName;

    @ColumnDefault("1")
    @Column(name = "category_rank", nullable = false)
    private Integer categoryRank = 1;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime = Instant.now();

    @ColumnDefault("''")
    @Column(name = "category_avatar")
    private String avatar = "";

    @ColumnDefault("''")
    @Column(name = "category_info")
    private String info = "";

    @ColumnDefault("'0'")
    @Column(name = "is_muted")
    private Boolean isMuted = false;

    @ColumnDefault("0")
    @Column(name = "is_pending")
    private Boolean isPending = false;

    @ColumnDefault("''")
    @Column(name = "category_rule")
    private String rule = "";
//    @OneToMany(mappedBy = "category")
//    private Set<CategoryMember> categoryMembers = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "postCategory")
//    private Set<Post> posts = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "category")
//    private Set<ChatChannel> chatChannels = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PostCategory that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}