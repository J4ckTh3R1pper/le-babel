package dayp308.lebabel.controller.authenticated;

import dayp308.lebabel.Constants;
import dayp308.lebabel.entity.business.UserEditForm;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.user.UserDetailedProj;
import dayp308.lebabel.exception.FileUploadException;
import dayp308.lebabel.repository.UserRepository;
import dayp308.lebabel.service.FileService;
import dayp308.lebabel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/auth/user")
public class AuthenticatedUserController {
    private final FileService fileService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthenticatedUserController(FileService fileService, UserRepository userRepository, UserService userService) {
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Operation(summary = "更换用户头像")
    @Parameters({
            @Parameter(name = "image", description = "用户上传的头像"),
            @Parameter(name = Constants.JWT_HEADER_NAME, description = "请求的JWT Token", in = ParameterIn.HEADER)
    })
    @PostMapping(value = "/update_avatar")
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

    @Operation(summary = "更新用户信息")
    @Parameters({
            @Parameter(name = "form", description = "包含用户信息的表单"),
            @Parameter(name = Constants.JWT_HEADER_NAME, description = "请求的JWT Token", in = ParameterIn.HEADER)
    })
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateUser(@Valid UserEditForm form, Authentication auth) {
        User user = (User) auth.getPrincipal();
        user.setNickName(form.getNickName());
        user.setIntroduce(form.getIntroduce());
        user.setGender(form.getGender());
        userRepository.saveAndFlush(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "关注用户")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY),
            @Parameter(name = Constants.JWT_HEADER_NAME, description = "请求的JWT Token", in = ParameterIn.HEADER)
    })
    @PostMapping("/follow")
    public Boolean followUser(@RequestParam("id") User targetUser, Authentication auth) {
        return userService.followUser((User) auth.getPrincipal(), targetUser);
    }

    @Operation(summary = "获取当前登录用户的id")
    @Parameters({
            @Parameter(name = Constants.JWT_HEADER_NAME, description = "请求的JWT Token", in = ParameterIn.HEADER)
    })
    @GetMapping("/authorize")
    public UserDetailedProj attemptLogin(Authentication auth) {
        return userService.findUserDetailedProjById(((User) auth.getPrincipal()).getId());
    }

    @Operation(summary = "获取当前登录用户是否关注目标用户")
    @Parameters({
            @Parameter(name = "id", description = "用户id", in = ParameterIn.QUERY),
            @Parameter(name = Constants.JWT_HEADER_NAME, description = "请求的JWT Token", in = ParameterIn.HEADER)
    })
    @GetMapping("/get_followed")
    public Boolean getFollowed(@RequestParam("id") long id,
                               Authentication auth
    ) {
        return userService.getFollowed(((User) auth.getPrincipal()).getId(), id);
    }

}
