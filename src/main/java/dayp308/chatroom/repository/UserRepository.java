package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    <T> Optional<T> findByLoginName(String loginName, Class<T> type);
    User findByLoginNameAndPassword(String loginName, String password);
    boolean existsByLoginNameAndPassword(String loginName, String password);
    <T> List<T> findByNickNameContainingIgnoreCase(String nickName, Class<T> clazz);

    <T> T findById(Long id, Class<T> clazz);
}