package dayp308.chatroom.entity.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import dayp308.chatroom.entity.enums.Role;

public interface MemberMinimal {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Role getRole();
    Integer getExperience();
    String getTitle();
}
