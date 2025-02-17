package dayp308.chatroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class CategoryMemberId implements java.io.Serializable {
    private static final long serialVersionUID = 3033184338078385519L;
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoryMemberId entity = (CategoryMemberId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, categoryId);
    }

}