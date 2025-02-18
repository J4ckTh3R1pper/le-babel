package dayp308.chatroom.entity.dto;

import dayp308.chatroom.entity.*;
import dayp308.chatroom.entity.enums.Genders;
import lombok.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;

    private String loginName;

    private String nickName;

    private String headImgUrl = "/images/avatar/default.jpg";

    private String location = "";

    private String introduce = "";

    private Boolean userStatus = false;

    private Instant lastLoginTime = Instant.now();

    private Instant createTime = Instant.now();

    private Genders gender = Genders.UNKNOWN;

}
