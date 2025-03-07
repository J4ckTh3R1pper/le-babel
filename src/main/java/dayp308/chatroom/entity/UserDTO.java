package dayp308.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dayp308.chatroom.entity.enums.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public record UserDTO(Long id, String loginName, String nickName, String headImgUrl, String location, String introduce,
                      Boolean userLocked,
                      Instant lastLoginTime,
                      Instant createTime,
                      Gender gender) {
}
