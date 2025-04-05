package dayp308.chatroom.entity.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_category_member")
public class CategoryMember {
    @EmbeddedId
    private CategoryMemberId id = new CategoryMemberId(); // 不初始化id会造成NPE

    @MapsId("categoryId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.GUEST;

    @ColumnDefault("0")
    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "title", length = 15)
    private String title;

    @ColumnDefault("from_unixtime(0)")
    @Column(name = "mute_expiration_date")
    private Instant muteExpirationDate = Instant.EPOCH;

    @ColumnDefault("curtime()")
    @Column(name = "join_date")
    private Instant joinDate = Instant.now();

}