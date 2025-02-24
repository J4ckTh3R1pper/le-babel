package dayp308.chatroom.deprecated.mapper;
import dayp308.chatroom.deprecated.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
