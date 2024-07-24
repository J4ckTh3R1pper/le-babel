package dayp308.chatroom.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.bean.*;
import dayp308.chatroom.exception.FileUploadException;
import dayp308.chatroom.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final ChatServerService chatServerService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService service,
                          ChatServerService chatServerService,
                          FileService fileService) {
        this.userService = service;
        this.chatServerService = chatServerService;
        this.fileService = fileService;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping(value = "/add_user")
    @ResponseBody
    public String addUser(@RequestBody User newUser) {
        if (userService.addUser(newUser)) {
            return "login";
        }
        else return "reg_failed";
    }

    @GetMapping(value = "/get_user")
    @ResponseBody
    public String getUser(@RequestParam("userId") Integer userId) throws JsonProcessingException {
        User user = userService.getUserById(userId);
        user.setPassword(null);
        user.setToken(null);
        return objectMapper.writeValueAsString(user);
    }

    @RequestMapping(value = "/delete_user")
    public void deleteUserById(@RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        userService.delUserById(id);
        response.sendRedirect("/list_users");
    }

    @RequestMapping(value = "/edit_user")
    public String toEditPage(HttpServletRequest req, ModelMap model) throws Exception {
        User user = (User) req.getSession().getAttribute("user");
        if ( user != null ) {
            user.setPassword("");
            model.addAttribute("userData", objectMapper.writeValueAsString(user));
            return "edituser";
        }
        else throw new Exception();
    }
    @PostMapping(value = "/edit_user/update_avatar")
    @ResponseBody
    public String updateAvatar(@RequestPart("image") MultipartFile imageFile, @RequestPart("token") String token) {
        Integer userId = userService.getUserByToken(token).getUserId();
        if ( userId == null )
            return "{message: \"invalid credential\"}";
        try {
            User user;
            int update;
            String url = this.fileService.uploadImage(imageFile);
            if (url == null)
                throw new FileNotFoundException();
            user = new User();
            user.setAvatar(url);
            update = this.userService.updateUserById(userId, user);
            return "{status:\"success\", update:" + update + "}";
        } catch (Exception e) {
            return "{error: \"" + e.getMessage() +"\"";
        }
    }
    @PostMapping(value = "/edit_user/update_user")
    @ResponseBody
    public String updateUser(@RequestBody User user, HttpServletRequest req) {
        System.out.println(user);
        if (Objects.equals(((User) (req.getSession().getAttribute("user"))).getUserId(), user.getUserId()))
            return "{status:\"success\", update:"+ userService.updateUserById(user.getUserId(), user) + "}";
        else return "{status:\"operation not allowed\"}";
    }

}
