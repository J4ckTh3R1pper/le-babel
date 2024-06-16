package dayp308.chatroom.controller;
import dayp308.chatroom.bean.*;
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
import java.io.IOException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final ChatServerService chatServerService;

    @Autowired
    public UserController(UserService service,
                          ChatServerService chatServerService) {
        this.userService = service;
        this.chatServerService = chatServerService;
    }

    @PostMapping(value = "/add_user")
    @ResponseBody
    public String addUser(@RequestBody User newUser) {
        if (userService.addUser(newUser)) {
            return "login";
        }
        else return "reg_failed";
    }

    @RequestMapping(value = "/delete_user")
    public void deleteUserById(@RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        userService.delUserById(id);
        response.sendRedirect("/list_users");
    }

    @RequestMapping(value = "/edit_user")
    public String toEditPage(@RequestParam("id") Integer id, ModelMap model) throws Exception {
        User user = userService.getUserById(id);
        if ( user != null ) {
            model.addAttribute("userData", user);
            return "edituser";
        }
        else throw new Exception();
    }

    @PostMapping(value = "/edit_user/update_user")
    @ResponseBody
    public Integer updateUser(@RequestBody User user) {
        System.out.println(user);
        return userService.updateUserById(user.getUserId(), user);
    }

}
