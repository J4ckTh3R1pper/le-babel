package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.Subscription;
import dayp308.lebabel.bean.entity.id.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
