package dayp308.lebabel.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.captcha.GifCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import dayp308.lebabel.entity.business.CaptchaResponse;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.user.UserDetailedProj;
import dayp308.lebabel.entity.business.UserEditForm;
import dayp308.lebabel.entity.business.UserRegistrationForm;
import dayp308.lebabel.entity.user.UserMinimal;
import dayp308.lebabel.entity.user.UserMinimalImpl;
import dayp308.lebabel.exception.FileUploadException;
import dayp308.lebabel.exception.InvalidFormException;
import dayp308.lebabel.repository.CategoryMemberRepository;
import dayp308.lebabel.repository.RedisCaptchaRepository;
import dayp308.lebabel.repository.UserRepository;
import dayp308.lebabel.service.FileService;
import dayp308.lebabel.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {

    private final CategoryMemberRepository categoryMemberRepository;
    private final UserService userService;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final RedisCaptchaRepository redisCaptchaRepository;

    public UserController(UserService service, FileService fileService, UserRepository userRepository, ObjectMapper jacksonObjectMapper, RedisCaptchaRepository redisCaptchaRepository, CategoryMemberRepository categoryMemberRepository) {
        this.userService = service;
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.redisCaptchaRepository = redisCaptchaRepository;
        this.categoryMemberRepository = categoryMemberRepository;
    }

    @PostMapping(value = "/api/no_auth/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationForm form) throws InvalidFormException, EntityExistsException, JsonProcessingException {
        if ( !redisCaptchaRepository.checkCaptcha(form.getUuid(), form.getCaptcha()) )
            throw new InvalidFormException("Invalid captcha", InvalidFormException.ErrorCode.INVALID_CAPTCHA);
        return new ResponseEntity<>(jacksonObjectMapper.writeValueAsString(userService.register(form)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/no_auth/captcha")
    public ResponseEntity<String> getCaptchaGet() throws JsonProcessingException {
        Captcha cap = new GifCaptcha(130, 48, 5);
        String uuid = redisCaptchaRepository.saveCaptcha(cap);
        String resp = jacksonObjectMapper.writeValueAsString(
                new CaptchaResponse(cap.toBase64(), uuid));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/api/no_auth/user/get_full_info")
    public UserDetailedProj getFullInfo(
        @RequestParam(value = "id") Long userId
    ) {
        UserDetailedProj user = userService.findUserDetailedProjById(userId);
        return user;
    }

    @GetMapping("/api/user/authorize")
    public UserDetailedProj attemptLogin(Authentication auth) {
        return userService.findUserDetailedProjById(((User) auth.getPrincipal()).getId());
    }
    
    @GetMapping("/api/user/get_followed")
    public Boolean getFollowed(@RequestParam("id") long id,
        Authentication auth
    ) {
        return userService.getFollowed(((User) auth.getPrincipal()).getId(), id);
    }
    

    @GetMapping(value = "/api/no_auth/user/get_minimal")
    public UserMinimal getMinimal(
            @RequestParam(value = "id", required = false) Long userId,
            Authentication auth
        ) {
        UserMinimal user;
        if (userId != null)
            user = userRepository.findById(userId, UserMinimal.class);
        else if (auth != null) {
            User detail =  (User) auth.getPrincipal();
            user = new UserMinimalImpl(detail.getNickName(), detail.getHeadImgUrl(), detail.getLocation());
        } else throw new NoResultException();
        return user;
    }

    @PostMapping(value = "/api/user/update_avatar")
    public String updateAvatar(
            @RequestPart("image") MultipartFile imageFile,
            Authentication auth
    ) throws FileUploadException {

        User user = ((User) auth.getPrincipal());
        String storeName = fileService.uploadImage(imageFile);
        user.setHeadImgUrl("/api/images/" + storeName);
        userRepository.saveAndFlush(user);
        return user.getHeadImgUrl();
    }

    @PostMapping(value = "/api/user/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateUser(@Valid UserEditForm form, Authentication auth) {
        User user = (User) auth.getPrincipal();
        user.setNickName(form.getNickName());
        user.setIntroduce(form.getIntroduce());
        user.setGender(form.getGender());
        userRepository.saveAndFlush(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/user/get_overall_exp")
    public Integer getOverallExp(@RequestParam("id") long userId) {
        int exp = userService.getOverallExp(userRepository.getReferenceById(userId));
        return exp;
    }

    @GetMapping("/api/no_auth/user/get_joined_category")
    public List<Integer> getJoinedCategory(
        @RequestParam("id") User user
    ) {
        return categoryMemberRepository.findCategoryIdsByJoinUserId(user.getId());
    }

    @PostMapping("/api/user/follow")
    public Boolean followUser(@RequestParam("id") User targetUser, Authentication auth) {
        return userService.followUser((User) auth.getPrincipal(), targetUser);
    }
    

}
