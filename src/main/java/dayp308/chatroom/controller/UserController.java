package dayp308.chatroom.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.business.UserEditForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.UserDTO;
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
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final FileService fileService;
    private final UserRepository userRepository;

    public UserController(UserService service, FileService fileService, UserRepository userRepository) {
        this.userService = service;
        this.objectMapper = new ObjectMapper();
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/api/user/register")
    public ResponseEntity<String> register(@Valid UserRegistrationForm form) throws InvalidFormException, EntityExistsException, JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(userService.register(form)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/user/get_user_by_id")
    public String getUser(@RequestParam("userId") Long userId) throws JsonProcessingException {
        UserDTO user = userService.findById(userId);
        return objectMapper.writeValueAsString(user);
    }

    @PostMapping(value = "/api/user/update_avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("token") String token
    ) throws FileUploadException {

        UserDTO user = null;
        user.setHeadImgUrl(fileService.uploadImage(imageFile));
        return new ResponseEntity<>(userService.updateUser(user).getHeadImgUrl(), HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateUser(@Valid UserEditForm form) {
        UserDTO user = null;
        BeanUtils.copyProperties(form, user);
        userService.updateUser(user);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/user/get_overall_exp")
    public ResponseEntity<String> getOverallExp(@RequestParam("userId") long userId) {
        int exp = userService.getOverallExp(userRepository.getReferenceById(userId));
        return new ResponseEntity<>(exp + "", HttpStatus.OK);
    }
}
