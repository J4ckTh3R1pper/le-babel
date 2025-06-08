package dayp308.lebabel.service;

import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.user.UserDetailedProj;
import dayp308.lebabel.entity.Subscription;
import dayp308.lebabel.entity.business.UserRegistrationForm;
import dayp308.lebabel.entity.id.SubscriptionId;
import dayp308.lebabel.entity.user.UserDTO;
import dayp308.lebabel.exception.InvalidFormException;
import dayp308.lebabel.exception.UserExistsException;
import dayp308.lebabel.repository.CategoryMemberRepository;
import dayp308.lebabel.repository.SubscriptionRepository;
import dayp308.lebabel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static dayp308.lebabel.repository.specification.UserSpecs.userMemberships;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryMemberRepository categoryMemberRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService, PasswordEncoder passwordEncoder, CategoryMemberRepository categoryMemberRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
        this.categoryMemberRepository = categoryMemberRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userRepository.findById(id, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDetailedProj findUserDetailedProjById(Long id) {
        return userRepository.findDetailById(id);
    }

    /***
     *
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public int getOverallExp(User user) {
        final AtomicInteger  overallExp = new AtomicInteger(0);
        categoryMemberRepository.findAll(userMemberships(user)).forEach(m -> {
            overallExp.addAndGet( m.getExperience() );
        });
        return overallExp.get();
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
     * @param form Controller层传入的表单
     * @return 注册完成的用户的ID
     * @throws UserExistsException 邮箱或用户名重复时抛出
     */
    public long register(UserRegistrationForm form) {
        User user = new User();
        if (userRepository.existsByLoginName(form.getLoginName())
            ) throw new InvalidFormException("e-mail已存在！", InvalidFormException.ErrorCode.INVALID_LOGIN_NAME);
        user.setLoginName(form.getLoginName());

        if (userRepository.existsByNickName(form.getNickName())
        ) throw new InvalidFormException("昵称已存在！",  InvalidFormException.ErrorCode.INVALID_NICKNAME);
        user.setNickName(form.getNickName());

        user.setPassword(passwordEncoder.encode(form.getPassword()));

            userRepository.saveAndFlush(user);
        return user.getId();
    }

    public Boolean getFollowed(long userId, long subscribedUserId) {
        SubscriptionId id = new SubscriptionId(userId, subscribedUserId);
        Subscription sub = subscriptionRepository.findById(id).orElse(null);
        if (sub != null) return true;
        return false;
    }

    public Boolean followUser(User user, User targetUser) {
        SubscriptionId id = new SubscriptionId(user.getId(), targetUser.getId());
        if (!subscriptionRepository.existsById(id)) {
            Subscription sub = new Subscription();
            sub.setUser(user);
            sub.setSubscribedUser(targetUser);
            subscriptionRepository.saveAndFlush(sub);
            return true;
        } else {
            subscriptionRepository.deleteById(id);
            return false;
        }
    }
}
