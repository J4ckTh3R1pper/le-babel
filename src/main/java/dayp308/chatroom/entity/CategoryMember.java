package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_category_member")
@DynamicInsert
@DynamicUpdate
public class CategoryMember {
    @EmbeddedId
    private CategoryMemberId id = new CategoryMemberId(); // 不初始化id会造成NPE

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Enumerated(EnumType.STRING)
    @Generated
    @Column(name = "role", nullable = false)
    private Roles role = Roles.SUBSCRIBER;

    @ColumnDefault("0")
    @Generated
    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "title", length = 15)
    private String title;

    @ColumnDefault("from_unixtime(0)")
    @Column(name = "mute_expiration_date")
    @Generated
    private Instant muteExpirationDate = Instant.EPOCH;

}