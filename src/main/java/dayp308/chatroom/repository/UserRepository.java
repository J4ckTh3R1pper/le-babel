package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByLoginName(String loginName);
    User findByLoginNameAndPassword(String loginName, String password);
    boolean existsByLoginNameAndPassword(String loginName, String password);
    List<User> findByNickNameContainingIgnoreCase(String nickName);

}