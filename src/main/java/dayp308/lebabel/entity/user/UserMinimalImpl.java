package dayp308.lebabel.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMinimalImpl implements UserMinimal {
    private String nickName;
    private String headImgUrl;
    private String location;
}