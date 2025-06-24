package dayp308.lebabel.bean.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UserMinimal")
public class UserMinimalRedis {
    @Id String id;
    private String nickName;
    private String headImgUrl;
    private String location;
}