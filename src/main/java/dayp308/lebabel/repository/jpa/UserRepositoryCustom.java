package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.user.UserDetailedProjection;

public interface UserRepositoryCustom {

    UserDetailedProjection getUserDetailedProj(Long id);
}
