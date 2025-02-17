package dayp308.chatroom.entity.dto;

import dayp308.chatroom.entity.*;
import dayp308.chatroom.entity.enums.Genders;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;

    private String loginName;

    private String nickName;

    private String headImgUrl;

    private String location;

    private String introduce;

    private Boolean userStatus = false;

    private Instant lastLoginTime;

    private Instant createTime;

    private Genders gender;
}
