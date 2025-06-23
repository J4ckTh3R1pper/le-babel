package dayp308.lebabel.bean.entity.user;

import dayp308.lebabel.enumeration.Gender;

import java.time.Instant;

public record UserDTO(
        Long id,
        String loginName,
        String nickName,
        String headImgUrl,
        String location,
        String introduce,
        Boolean locked,
        Instant lastLoginTime,
        Instant createTime,
        Gender gender) {
}
