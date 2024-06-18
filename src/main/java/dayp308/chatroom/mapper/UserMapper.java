package dayp308.chatroom.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import dayp308.chatroom.bean.*;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int addUser(@Param("user") User user);
    void delUserById(@Param("id") Integer id);
    boolean updateUserById(@Param("id") Integer id, @Param("user") User user);

    User getUserLogin(@Param("username") String username, @Param("password") String password);
    User getUserByUsername(@Param("username") String username);
    List<User> searchUserByText(@Param("text") String text);
    User getUserById(@Param("id") Integer id);

    List<User> getAllUsers();

	User getUserByToken(@Param("token") String token);
}
