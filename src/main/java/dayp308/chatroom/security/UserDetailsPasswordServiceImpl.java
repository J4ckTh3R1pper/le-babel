package dayp308.chatroom.security;

import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final UserRepository userRepository;
    @Autowired
    public UserDetailsPasswordServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        User oldUser = (User) user;
        oldUser.setPassword(newPassword);
        return userRepository.saveAndFlush(oldUser);
    }
}
