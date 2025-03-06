package dayp308.chatroom.service;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.UserDTO;
import dayp308.chatroom.exception.UserExistsException;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static dayp308.chatroom.repository.specification.CustomSpecifications.userMemberships;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryMemberRepository categoryMemberRepository;

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService, PasswordEncoder passwordEncoder, CategoryMemberRepository categoryMemberRepository) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
        this.categoryMemberRepository = categoryMemberRepository;
    }

    /***
     *
     * @param loginName 用户输入的邮箱
     * @param password 用户输入的加密密码
     * @return 用户或密码错误时返回false
     */
    @Transactional(readOnly = true)
    public boolean checkLogin(String loginName, String password) {
        return userRepository.existsByLoginNameAndPassword(loginName, password);
    }

    @Transactional(readOnly = true)
    public UserDTO login(String loginName, String plainPassword) throws Exception {
        String passwordMd5 = DigestUtils.md5Hex(plainPassword);
        if (!userRepository.existsByLoginNameAndPassword(loginName, DigestUtils.md5Hex(passwordMd5))) {
            throw new Exception("email or password incorrect");
        }
        User user = userRepository.findByLoginNameAndPassword(loginName, passwordMd5);
        return conversionService.convert(user, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return conversionService.convert(userRepository.findById(id), UserDTO.class);
    }

    /***
     *
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public int getOverallExp(User user) {
        final int[] overallExp = {0};
        categoryMemberRepository.findAll(userMemberships(user)).forEach(m -> {
            overallExp[0] = overallExp[0] + m.getExperience();
        });
        return overallExp[0];
    }

    /***
     *
     * @param loginName 要搜索用户的邮箱
     * @return 搜索到用户的UserDTO类
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName, UserDTO.class);
    }

    /***
     *
     * @param nickName 搜索用户名的关键词
     * @return 搜索到用户的DTO类集合
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchByNickName(String nickName) {
        List<UserDTO> DOList = userRepository.findByNickNameContainingIgnoreCase(nickName, UserDTO.class);
        return DOList;
    }

    /*** 注册用户
     *
     * @param form Controller层传入的DTO类
     * @return 注册完成的用户DTO对象
     * @throws UserExistsException 邮箱或用户名重复时抛出
     */
    public long register(UserRegistrationForm form) {
        User user = new User();
        user.setLoginName(form.getLoginName());
        user.setNickName(form.getNickName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User e-mail already exists");
        }
        return user.getId();
    }

    public UserDTO updateUser(User user) {
        userRepository.saveAndFlush(user);
        return userRepository.findById(user.getId(), UserDTO.class);
    }

}
