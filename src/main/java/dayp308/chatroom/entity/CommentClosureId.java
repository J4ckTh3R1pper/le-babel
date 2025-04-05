package dayp308.chatroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CommentClosureId implements java.io.Serializable {
    private static final long serialVersionUID = 7924468670190188964L;
    @NotNull
    @Column(name = "ancestor", nullable = false)
    private Long ancestor;

    @NotNull
    @Column(name = "descendant", nullable = false)
    private Long descendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommentClosureId entity = (CommentClosureId) o;
        return Objects.equals(this.ancestor, entity.ancestor) &&
                Objects.equals(this.descendant, entity.descendant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ancestor, descendant);
    }

}