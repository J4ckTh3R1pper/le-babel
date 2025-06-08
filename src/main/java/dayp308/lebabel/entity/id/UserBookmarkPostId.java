package dayp308.lebabel.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class UserBookmarkPostId implements java.io.Serializable {
    private static final long serialVersionUID = -9180015529837495002L;
    @ColumnDefault("0")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ColumnDefault("0")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserBookmarkPostId entity = (UserBookmarkPostId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

}