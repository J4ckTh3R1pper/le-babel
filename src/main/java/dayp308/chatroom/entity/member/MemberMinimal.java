package dayp308.chatroom.entity.member;

import dayp308.chatroom.entity.enums.Role;

public interface MemberMinimal {
    Role getRole();
    Integer getExperience();
    String getTitle();
}
