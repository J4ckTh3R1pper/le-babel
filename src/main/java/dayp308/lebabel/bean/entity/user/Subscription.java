package dayp308.lebabel.bean.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.lebabel.bean.entity.id.SubscriptionId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {
    @EmbeddedId
    private SubscriptionId subscriptionId = new SubscriptionId();

    @MapsId("userId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("subscribedUserId")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subscribed_user_id")
    private User subscribedUser;
}
