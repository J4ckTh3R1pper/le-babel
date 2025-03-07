package dayp308.chatroom.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.UserDetailedProj;
import dayp308.chatroom.entity.business.UserEditForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.UserDTO;
import dayp308.chatroom.exception.FileUploadException;
import dayp308.chatroom.exception.InvalidFormException;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.FileService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    private final UserService userService;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;

    public UserController(UserService service, FileService fileService, UserRepository userRepository, ObjectMapper jacksonObjectMapper) {
        this.userService = service;
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @PostMapping(value = "/api/user/register")
    public ResponseEntity<String> register(@Valid UserRegistrationForm form) throws InvalidFormException, EntityExistsException, JsonProcessingException {
        return new ResponseEntity<>(jacksonObjectMapper.writeValueAsString(userService.register(form)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/no_auth/user/get_user_by_id")
    public ResponseEntity<String> getUser(@RequestParam("userId") Long userId) throws JsonProcessingException {
        UserDetailedProj user = userService.findUserDetailedProjById(userId);
        return new ResponseEntity<>(jacksonObjectMapper.writeValueAsString(user), HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/update_avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("token") String token,
            Authentication auth
    ) throws FileUploadException {

        User user = ((User) auth.getPrincipal());
        user.setHeadImgUrl(fileService.uploadImage(imageFile));
        return new ResponseEntity<>(userService.updateUser(user).headImgUrl(), HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateUser(@Valid UserEditForm form, Authentication auth) {
        User user = (User) auth.getPrincipal();
        user.setNickName(form.getNickName());
        user.setIntroduce(form.getIntroduce());
        user.setGender(form.getGender());
        userService.updateUser(user);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/user/get_overall_exp")
    public ResponseEntity<String> getOverallExp(@RequestParam("userId") long userId) {
        int exp = userService.getOverallExp(userRepository.getReferenceById(userId));
        return new ResponseEntity<>(exp + "", HttpStatus.OK);
    }
}
