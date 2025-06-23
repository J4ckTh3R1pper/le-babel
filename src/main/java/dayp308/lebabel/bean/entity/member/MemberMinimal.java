package dayp308.lebabel.bean.entity.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import dayp308.lebabel.enumeration.Role;

public interface MemberMinimal {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Role getRole();
    Integer getExperience();
    String getTitle();
}
