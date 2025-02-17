package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_post_category")
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 16)
    private String categoryName;

    @ColumnDefault("1")
    @Column(name = "category_rank", nullable = false)
    private Integer categoryRank;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ColumnDefault("current_timestamp()")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @OneToMany(mappedBy = "category")
    private Set<CategoryMember> categoryMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "postCategory")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<ChatChannel> chatChannels = new LinkedHashSet<>();

}