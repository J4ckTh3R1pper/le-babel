package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Role;

import java.time.Instant;


public interface CategoryMemberDTO {
    User getUser();
    Category getCategory();
    Role getRole();
    String getTitle();
    Integer getExperience();
    Instant getMuteExpirationDate();

    interface User {
        Long getId();
    }

    interface Category {
        Integer getId();
    }
}
