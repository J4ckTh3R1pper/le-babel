package dayp308.chatroom.service;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.exception.InvalidRegistrationFormException;
import dayp308.chatroom.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    /***
     *
     * @param loginName 用户输入的邮箱
     * @param passwordMd5 用户输入的加密密码
     * @return 用户或密码错误时返回false
     */
    public boolean checkLogin(String loginName, String passwordMd5) {
        return (userRepository.findByLoginNameAndPasswordMd5(loginName, passwordMd5) != null);
    }

    /***
     *
     * @param loginName 要搜索用户的邮箱
     * @return 搜索到用户的UserDTO类
     */
    public UserDTO findByLoginName(String loginName) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(userRepository.findByLoginName(loginName), dto);
        return dto;
    }

    /***
     *
     * @param nickName 搜索用户名的关键词
     * @return 搜索到用户的DTO类集合
     */
    public List<UserDTO> findByNickName(String nickName) {
        List<User> DOList = userRepository.findByNickName(nickName);

        return DOList.stream().map(u -> conversionService.convert(u, UserDTO.class)).toList();
    }

    /*** 注册用户
     *
     * @param userDTO Controller层传入的DTO类
     * @param password 密码
     * @return 注册完成的用户DTO对象
     * @throws InvalidRegistrationFormException 邮箱无效或用户名重复或密码长度不足时抛出
     * @throws EntityExistsException 邮箱或用户名重复时抛出
     */
    public UserDTO register(UserDTO userDTO, String password) throws InvalidRegistrationFormException, EntityExistsException {
        if ( userDTO.getLoginName() == null || !EmailValidator.getInstance().isValid(userDTO.getLoginName()) )
            throw new InvalidRegistrationFormException("invalid email", 100);

        if ( userDTO.getNickName() == null || userDTO.getNickName().isBlank() )
            throw new InvalidRegistrationFormException("invalid nickname", 101);

        if ( password == null || password.length() < 8 )
            throw new InvalidRegistrationFormException("invalid password", 102);

        User user = conversionService.convert(userDTO, User.class);
        user.setPasswordMd5(DigestUtils.md5Hex(password));
        userRepository.addUser(user);
        return conversionService.convert(user, UserDTO.class);
    }
}
