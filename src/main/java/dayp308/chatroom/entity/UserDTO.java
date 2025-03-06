package dayp308.chatroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dayp308.chatroom.entity.enums.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public record UserDTO(Long id, String loginName, String nickName, String headImgUrl, String location, String introduce,
                      Boolean userLocked,
                      @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") @DateTimeFormat(pattern = "yyyy-MM-dd") Instant lastLoginTime,
                      @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") @DateTimeFormat(pattern = "yyyy-MM-dd") Instant createTime,
                      Gender gender) {
}
