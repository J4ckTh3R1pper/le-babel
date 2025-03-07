package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.UserDTO;
import dayp308.chatroom.entity.UserDetailedProj;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    <T> Optional<T> findByLoginName(String loginName, Class<T> type);
    User findByLoginNameAndPassword(String loginName, String password);
    boolean existsByLoginNameAndPassword(String loginName, String password);
    <T> List<T> findByNickNameContainingIgnoreCase(String nickName, Class<T> clazz);

    <T> T findById(Long id, Class<T> clazz);

    @Query("""
            SELECT  u.id AS id,
                    u.nickName AS nickName,
                    u.gender AS gender,
                    u.lastLoginTime AS lastLoginTime,
                    u.createTime AS createTime,
                    u.headImgUrl AS headImgUrl,
                    u.introduce AS introduce,
                    u.location AS location,
                    u.userLocked AS userLocked,
                    COUNT(CASE WHEN u.id = s.user.id THEN 1 END) AS followingCount,
                    COUNT(CASE WHEN u.id = s.subscribedUser.id THEN 1 END) AS followerCount
            FROM User u
            LEFT JOIN Subscription s ON u.id = s.user.id OR s.subscribedUser.id = u.id
            WHERE u.id = ?1
            """)
    UserDetailedProj findDetailById(Long userId);

    @Query("""
            SELECT  u.id AS id,
                    u.nickName AS nickName,
                    u.gender AS gender,
                    u.lastLoginTime AS lastLoginTime,
                    u.createTime AS createTime,
                    u.headImgUrl AS headImgUrl,
                    u.introduce AS introduce,
                    u.location AS location,
                    u.userLocked AS userLocked,
                    COUNT(CASE WHEN u.id = s.user.id THEN 1 END) AS followingCount,
                    COUNT(CASE WHEN u.id = s.subscribedUser.id THEN 1 END) AS followerCount
            FROM User u
            LEFT JOIN Subscription s ON u.id = s.user.id OR s.subscribedUser.id = u.id
            GROUP BY u.id
            """)
    Slice<UserDetailedProj> findDetailsById(Pageable pageable);
}