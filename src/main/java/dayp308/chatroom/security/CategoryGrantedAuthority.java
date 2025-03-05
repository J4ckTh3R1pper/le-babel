package dayp308.chatroom.security;

import dayp308.chatroom.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;

public class CategoryGrantedAuthority implements GrantedAuthority {
    int categoryId;
    Role role;
    @Override
    public String getAuthority() {
        return this.categoryId + "_" + this.role.toString();
    }
}
