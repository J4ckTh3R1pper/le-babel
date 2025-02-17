package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "tb_category_member")
public class CategoryMember {
    @EmbeddedId
    private CategoryMemberId id;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @ColumnDefault("0")
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "title", length = 15)
    private String title;

    @Column(name = "mute_expiration_date")
    private Instant muteExpirationDate;

}