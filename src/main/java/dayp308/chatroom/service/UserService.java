package dayp308.chatroom.service;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDTO> findByNickName(String nickName) {
        List<User> DOList = userRepository.findByNickName(nickName);

        return DOList.stream().map(u -> conversionService.convert(u, UserDTO.class)).toList();
    }

}
