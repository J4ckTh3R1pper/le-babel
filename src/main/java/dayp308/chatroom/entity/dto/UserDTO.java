package dayp308.chatroom.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dayp308.chatroom.entity.*;
import dayp308.chatroom.entity.enums.Genders;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserDTO {
    private Long id;

    private String loginName;

    private String nickName;

    private String headImgUrl = "/images/avatar/default.jpg";

    private String location = "";

    private String introduce = "";

    private Boolean userStatus = false;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant lastLoginTime = Instant.now();

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant createTime = Instant.now();

    private Genders gender = Genders.UNKNOWN;

}
