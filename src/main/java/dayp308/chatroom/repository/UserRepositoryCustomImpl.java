package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.User_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /*** 以用户名关键词搜索用户
     *
     * @param nickName 用户名关键词
     *
     */
    public List<User> findByNickName(String nickName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        cq.where(cb.like(root.get(User_.NICK_NAME), "%" + nickName + "%"));

        return em.createQuery(cq).getResultList();
    }

}
