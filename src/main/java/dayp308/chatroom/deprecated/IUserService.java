package dayp308.chatroom.deprecated;
import dayp308.chatroom.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IUserService {
    boolean addUser(User user);

    void delUserById(Integer id);

    Integer updateUserById(Integer id, User newUser);

    User getUserLogin(String username, String password);

    PageInfo<User> searchUserByText(Integer page, String text);

    User getUserById(Integer id);

    List<User> getAllUsers();

    PageInfo<User> getPagedUsers(Integer page);

	User getUserByToken(String token);

}
