package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.exception.UserExistedException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import dayp308.chatroom.entity.User_;
import java.util.List;

@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User, Long> {
    @PersistenceContext
    private EntityManager em;

    /*** 以邮箱搜索用户
     *
     * @param loginName 邮箱
     *
     */
    public User findByLoginName(String loginName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        cq.where(cb.equal(root.get(User_.LOGIN_NAME), loginName));

        try {
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

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

    /*** 以用户名和加密密码查找用户(验证登录)
     *
     * @param loginName 邮箱
     * @param passwordMd5 加密密码
     *
     */
    public User findByLoginNameAndPasswordMd5(String loginName, String passwordMd5) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        cq.where(
                cb.and(
                        cb.equal(root.get(User_.LOGIN_NAME), loginName),
                        cb.equal(root.get(User_.passwordMd5), passwordMd5)
                )
        );

        try {
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /***
     *
     * @param user 要创建用户的类
     * @return 用户创建成功后的主键
     * @throws EntityExistsException 注册邮箱已存在时抛出异常
     */
    @Transactional
    public long addUser(User user) throws EntityExistsException {
        if ( this.findByLoginName(user.getLoginName()) != null ) {
            throw new EntityExistsException();
        }
        em.persist(user);
        return user.getId();
    }

    @Transactional
    public User updateUser(User user) {
        em.merge(user);
        return user;
    }
}