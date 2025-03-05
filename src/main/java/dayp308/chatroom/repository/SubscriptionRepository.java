package dayp308.chatroom.repository;

import dayp308.chatroom.entity.Subscription;
import dayp308.chatroom.entity.id.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
