package dayp308.chatroom.entity;

import dayp308.chatroom.entity.id.SubscriptionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {
    @EmbeddedId
    private SubscriptionId subscriptionId = new SubscriptionId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("subscribedUserId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subscribed_user_id")
    private User subscribedUser;
}
