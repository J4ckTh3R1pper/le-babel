package dayp308.lebabel.bean.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3033184338078385519L;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "subscribed_user_id", nullable = false)
    private Long subscribedUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubscriptionId entity = (SubscriptionId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.subscribedUserId, entity.subscribedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, subscribedUserId);
    }


}
