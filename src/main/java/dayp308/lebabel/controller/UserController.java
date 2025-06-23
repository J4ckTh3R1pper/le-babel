package dayp308.lebabel.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.captcha.GifCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import dayp308.lebabel.bean.view.CaptchaResponse;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.user.UserDetailedProjection;
import dayp308.lebabel.bean.ao.UserRegistrationForm;
import dayp308.lebabel.bean.entity.user.UserMinimal;
import dayp308.lebabel.bean.entity.user.UserMinimalImpl;
import dayp308.lebabel.exception.InvalidFormException;
import dayp308.lebabel.repository.jpa.CategoryMemberRepository;
import dayp308.lebabel.repository.redis.RedisCaptchaRepository;
import dayp308.lebabel.repository.jpa.UserRepository;
import dayp308.lebabel.service.FileService;
import dayp308.lebabel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/no_auth/user")
@Tag(name = "用户接口")
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

    @Operation(summary = "用户注册")
    @Parameters({
            @Parameter(name = "form", description = "包含用户注册所需信息的表单", required = true)
    })
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationForm form) throws InvalidFormException, EntityExistsException, JsonProcessingException {
        if ( !redisCaptchaRepository.checkCaptcha(form.getUuid(), form.getCaptcha()) )
            throw new InvalidFormException("Invalid captcha", InvalidFormException.ErrorCode.INVALID_CAPTCHA);
        return new ResponseEntity<>(jacksonObjectMapper.writeValueAsString(userService.register(form)), HttpStatus.CREATED);
    }

    @Operation(summary = "生成并获取图片验证码")
    @GetMapping(value = "/captcha")
    public ResponseEntity<String> getCaptchaGet() throws JsonProcessingException {
        Captcha cap = new GifCaptcha(130, 48, 5);
        String uuid = redisCaptchaRepository.saveCaptcha(cap);
        String resp = jacksonObjectMapper.writeValueAsString(
                new CaptchaResponse(cap.toBase64(), uuid));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(summary = "获取用户完整信息")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/get_full_info")
    public UserDetailedProjection getFullInfo(
        @RequestParam("id") Long userId
    ) {
        UserDetailedProjection user = userService.findUserDetailedProjById(userId);
        return user;
    }

    @Operation(summary = "获取用户的基本信息，包括头像、用户名和登录地")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/get_minimal")
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

    @Operation(summary = "获取用户总经验")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY)
    })
    @GetMapping("/get_overall_exp")
    public Integer getOverallExp(@RequestParam("id") long userId) {
        int exp = userService.getOverallExp(userRepository.getReferenceById(userId));
        return exp;
    }

    @Operation(summary = "获取用户所关注的板块的id列表")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY)
    })
    @GetMapping("/get_joined_category")
    public List<Integer> getJoinedCategory(
        @RequestParam("id") User user
    ) {
        return categoryMemberRepository.findCategoryIdsByJoinUserId(user.getId());
    }

}
