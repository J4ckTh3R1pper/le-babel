package dayp308.chatroom.controller;

import com.github.pagehelper.PageInfo;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.ServerMember;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.ChatServerService;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class ChatServerController {
    private final UserService userService;
    private final ChatServerService chatServerService;

    @Autowired
    public ChatServerController(UserService userService, ChatServerService chatServerService) {
        this.userService = userService;
        this.chatServerService = chatServerService;
    }

    @RequestMapping(value = "/invite_user")
    public void addUserToServer(@RequestParam("userId") Integer userId,
                                @RequestParam("serverId") Integer serverId,
                                HttpServletRequest req,
                                HttpServletResponse resp
                                ) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if ( chatServerService.getUserIdentityOfServer(serverId, user.getUserId()).getIndex() < 1 )
            resp.sendRedirect("/chat");
        else if ( chatServerService.getUserIdentityOfServer(serverId, userId).getIndex() >= 0 )
            resp.sendRedirect("/list_users?server=" + serverId);
        else chatServerService.addUser(serverId, userId);
        resp.sendRedirect("/list_users?server=" + serverId);
    }

    @RequestMapping(value = "/banish_user")
    public void removeUserFromServer(@RequestParam("userId") Integer userId,
                                     @RequestParam("server") Integer serverId,
                                     HttpServletRequest req,
                                     HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity.getIndex() < targetIdentity.getIndex() )
            resp.sendRedirect("/list_users?server=" + serverId);
        else if ( selfIdentity == Identity.OWNER )
            resp.sendRedirect("/list_users?server=" + serverId);
        else chatServerService.removeUser(serverId, userId);
        resp.sendRedirect("/list_users?server=" + serverId);
    }

    @PostMapping(value = "/get_username", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUname(@RequestParam("userId") String userId, @RequestParam("serverId") String serverId) {
        User user = userService.getUserById(Integer.parseInt(userId));
        String nickname = chatServerService.getUserNicknameOfServer(Integer.parseInt(serverId), user);
        return "{\"user_id\":\"" + user.getUserId() + "\",\"nickname\":\"" + nickname + "\",\"avatar\":\"" + user.getAvatar() + "\"}";
    }

    @RequestMapping(value = "/list_users")
    public String toList(@RequestParam(name = "page", defaultValue = "1") @Nullable Integer page,
                         @RequestParam("searchText") @Nullable String searchText,
                         @RequestParam("server") Integer serverId,
                         ModelMap modelMap,
                         HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if ( chatServerService.getUserIdentityOfServer(serverId, user.getUserId()).getIndex() < 1 )
            return "chat";

        if (page == null)
            page = 1;

        PageInfo<ServerMember> serverMembers;
        if (searchText == null)
            serverMembers = chatServerService.getPagedUsers(page, serverId);
        else serverMembers = chatServerService.searchUserByText(page, searchText, serverId);
        modelMap.addAttribute("serverMembers", serverMembers);
        modelMap.addAttribute("searchText", searchText);
        modelMap.addAttribute("serverId", serverId);
        return "list";
    }

}
