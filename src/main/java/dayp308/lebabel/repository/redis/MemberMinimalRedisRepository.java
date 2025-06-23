package dayp308.lebabel.repository.redis;

import dayp308.lebabel.bean.entity.member.MemberMinimalRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMinimalRedisRepository extends CrudRepository<MemberMinimalRedis, String> {
    <T> Optional<T> findById(String id, Class<T> clazz);
}
