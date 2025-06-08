package dayp308.lebabel.entity.category;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dayp308.lebabel.entity.member.CategoryMember;
import dayp308.lebabel.entity.ChatChannel;
import dayp308.lebabel.entity.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_post_category")
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 16, unique = true)
    private String name;

    @ColumnDefault("1")
    @Column(name = "category_rank", nullable = false)
    private Integer rank = 1;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private Set<CategoryMember> categoryMembers = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private Set<Post> posts = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private Set<ChatChannel> chatChannels = new LinkedHashSet<>();

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