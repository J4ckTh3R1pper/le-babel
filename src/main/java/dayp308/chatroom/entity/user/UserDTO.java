package dayp308.chatroom.entity.user;

import dayp308.chatroom.entity.enums.Gender;

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
