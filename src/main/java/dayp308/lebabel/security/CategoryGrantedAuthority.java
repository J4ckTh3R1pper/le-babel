package dayp308.lebabel.security;

import dayp308.lebabel.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;

public class CategoryGrantedAuthority implements GrantedAuthority {
    int categoryId;
    Role role;
    @Override
    public String getAuthority() {
        return this.categoryId + "_" + this.role.toString();
    }
}
