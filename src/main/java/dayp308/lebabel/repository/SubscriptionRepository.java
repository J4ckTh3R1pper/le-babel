package dayp308.lebabel.repository;

import dayp308.lebabel.entity.Subscription;
import dayp308.lebabel.entity.id.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
