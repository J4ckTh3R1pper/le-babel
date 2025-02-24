package dayp308.chatroom.deprecated.service;
import dayp308.chatroom.deprecated.model.User;
import dayp308.chatroom.deprecated.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService implements IUserService {
    private final UserMapper userMapper;


    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public boolean addUser(User user) {
        if ( userMapper.getUserByUsername(user.getUsername()) == null )
        {
            String token = DigestUtils.sha256Hex(user.getUsername() +  user.getPassword());
            user.setToken(DigestUtils.sha256Hex(token + (Math.random() * 10000) ));
            userMapper.addUser(user);
            return true;
        }
        return false;
    }
    @Override
    public User getUserLogin(String username, String password)
    {
        return userMapper.getUserLogin(username, password);
    }

	@Override
	public User getUserByToken(String token) {
		return userMapper.getUserByToken(token);
	}

    @Override
    public PageInfo<User> searchUserByText(Integer page, String text) {
        PageHelper.startPage(page, 5);
        List<User> users = userMapper.searchUserByText(text);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        PageHelper.clearPage();
        return pageInfo;
    }

    @Override
    public void delUserById(Integer id) {
        if (userMapper.getUserById(id) != null)
            userMapper.delUserById(id);
    }

    @Override
    public Integer updateUserById(Integer id, User newUser) {
        if ( userMapper.getUserById(id) == null )
            return 3;
        if (userMapper.updateUserById(id, newUser))
            return 100;
        return 101;
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public PageInfo<User> getPagedUsers(Integer page) {
        PageHelper.startPage(page, 5);
        List<User> list = userMapper.getAllUsers();
        PageInfo<User> pageInfo = new PageInfo<>(list);
        PageHelper.clearPage();
        return pageInfo;
    }


}
