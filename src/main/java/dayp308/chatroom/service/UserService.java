package dayp308.chatroom.service;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
    }

    /***
     *
     * @param loginName 用户输入的邮箱
     * @param password 用户输入的加密密码
     * @return 用户或密码错误时返回false
     */
    public boolean checkLogin(String loginName, String password) {
        return userRepository.existsByLoginNameAndPassword(loginName, password);
    }

    public UserDTO login(String loginName, String plainPassword) throws Exception {
        String passwordMd5 = DigestUtils.md5Hex(plainPassword);
        if (!userRepository.existsByLoginNameAndPassword(loginName, DigestUtils.md5Hex(passwordMd5))) {
            throw new Exception("email or password incorrect");
        }
        User user = userRepository.findByLoginNameAndPassword(loginName, passwordMd5);
        return conversionService.convert(user, UserDTO.class);
    }

    public UserDTO findById(Long id) {
        return conversionService.convert(userRepository.findById(id), UserDTO.class);
    }


    /***
     *
     * @param loginName 要搜索用户的邮箱
     * @return 搜索到用户的UserDTO类
     */
    public Optional<User> findByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    /***
     *
     * @param nickName 搜索用户名的关键词
     * @return 搜索到用户的DTO类集合
     */
    public List<UserDTO> searchByNickName(String nickName) {
        List<User> DOList = userRepository.findByNickNameContainingIgnoreCase(nickName);

        return DOList.stream().map(u -> conversionService.convert(u, UserDTO.class)).toList();
    }

    /*** 注册用户
     *
     * @param form Controller层传入的DTO类
     * @return 注册完成的用户DTO对象
     * @throws EntityExistsException 邮箱或用户名重复时抛出
     */
    public UserDTO register(UserRegistrationForm form) throws EntityExistsException {
        User user = new User();
        user.setLoginName(form.getLoginName());
        user.setNickName(form.getNickName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.saveAndFlush(user);
        return conversionService.convert(user, UserDTO.class);
    }

    public UserDTO updateUser(UserDTO dto) {
        User user = conversionService.convert(dto, User.class);
        userRepository.saveAndFlush(user);
        return conversionService.convert(user, UserDTO.class);
    }

}
