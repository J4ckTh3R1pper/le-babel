package dayp308.chatroom.repository;

import dayp308.chatroom.entity.user.UserDetailedProj;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /*** 以用户名关键词搜索用户
     *
     * @param id 用户名关键词
     *
     */

    public UserDetailedProj getUserDetailedProj(Long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return null;
    }
}
