package dayp308.lebabel.bean.entity.member;

import dayp308.lebabel.enumeration.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "MemberMinimal")
@Data
public class MemberMinimalRedis {

    @Id private String id;
    private Role role;
    private Integer experience;
    private String title;
    @TimeToLive
    private Integer expiration;
    public MemberMinimalRedis() {}
    public MemberMinimalRedis(int categoryId, long userId, Role role, int experience, String title, int expiration) {
        this.id = categoryId + ":" + userId;
        this.role = role;
        this.experience = experience;
        this.title = title;
        this.expiration = expiration;
    }
}
