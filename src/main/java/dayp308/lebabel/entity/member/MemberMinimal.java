package dayp308.lebabel.entity.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import dayp308.lebabel.entity.enums.Role;

public interface MemberMinimal {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Role getRole();
    Integer getExperience();
    String getTitle();
}
