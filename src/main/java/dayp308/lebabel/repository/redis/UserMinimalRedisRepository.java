package dayp308.lebabel.repository.redis;

import dayp308.lebabel.bean.entity.user.UserMinimal;
import dayp308.lebabel.bean.entity.user.UserMinimalRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMinimalRedisRepository extends CrudRepository<UserMinimalRedis, String> {
    <T> Optional<T> findById(String id, Class<T> clazz);
}
