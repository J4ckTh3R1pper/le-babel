package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByToken(String token);
    User findByLoginName(String loginName);
    User findByLoginNameAndPasswordMd5(String loginName, String passwordMd5);
    boolean existsByLoginNameAndPasswordMd5(String loginName, String passwordMd5);
    List<User> findByNickNameContainingIgnoreCase(String nickName);

    boolean existsByToken(String token);
}