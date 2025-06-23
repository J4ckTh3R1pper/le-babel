package dayp308.lebabel.security;

import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginName(username, User.class).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userRepository.getReferenceById(id);
    }
}
