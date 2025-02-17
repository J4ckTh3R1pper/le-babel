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
public class UserBookmarkCommentId implements java.io.Serializable {
    private static final long serialVersionUID = 4585486529617563221L;
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserBookmarkCommentId entity = (UserBookmarkCommentId) o;
        return Objects.equals(this.commentId, entity.commentId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, userId);
    }

}