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
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Column(name = "category_name", nullable = false, length = 16)
    private String categoryName;

    @ColumnDefault("1")
    @Generated
    @Column(name = "category_rank", nullable = false)
    private Integer categoryRank;

    @ColumnDefault("0")
    @Generated
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ColumnDefault("current_timestamp()")
    @Generated
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("''")
    @Generated
    @Column(name = "category_avatar")
    private String categoryAvatar;

    @ColumnDefault("''")
    @Generated
    @Column(name = "category_info")
    private String categoryInfo;

    @OneToMany(mappedBy = "category")
    private Set<CategoryMember> categoryMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "postCategory")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<ChatChannel> chatChannels = new LinkedHashSet<>();

}