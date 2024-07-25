package dayp308.chatroom.controller;

import com.github.pagehelper.PageInfo;
import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.ServerMember;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.ChatServerService;
import dayp308.chatroom.service.FileService;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

import java.io.IOException;

@Controller
public class ChatServerController {
    private final UserService userService;
    private final ChatServerService chatServerService;

    private ObjectMapper objectMapper;
    private FileService fileService;

    @Autowired
    public ChatServerController(UserService userService, ChatServerService chatServerService, FileService fileService) {
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
        this.chatServerService = chatServerService;
        this.fileService = fileService;
    }
    @PostMapping(value = "/edit_server/update_server", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateServerInfo(@RequestParam("serverId") int serverId,
                                   @RequestParam("token") String token,
                                   @Nullable @RequestParam("description") String description,
                                   @Nullable @RequestParam("name") String name,
                                   HttpServletRequest req,
                                   HttpServletResponse resp
                                   ) {
        if (chatServerService.getUserIdentityOfServer(serverId, userService.getUserByToken(token).getUserId()).getIndex() < 2) {
            resp.setStatus(403);
            return "{\"code\":403}";
        }
        if ( (description == null || description.isBlank()) && (name == null || name.isBlank()) ) {
            resp.setStatus(418);
            return "{\"code\":418}";
        }
        ChatServer server = new ChatServer();
        if (description != null && !description.isBlank())
            server.setDescription(description);
        if (name != null && !name.isBlank())
            server.setName(name);
        return "{\"updates\":" + chatServerService.updateServerById(server, serverId) + "}";
    }
    @PostMapping(value = "/edit_server/update_avatar")
    @ResponseBody
    public String updateAvatar(@RequestParam("image") MultipartFile imageFile, @RequestParam("serverId") int serverId, @RequestParam("token") String token, HttpServletResponse resp) {
        Integer userId = userService.getUserByToken(token).getUserId();
        if ( userId == null )
            return "{message: \"invalid credential\"}";
        try {
            if ( chatServerService.getUserIdentityOfServer(serverId, userId).getIndex() < 1 )
                resp.setStatus(403);
            String url = this.fileService.uploadImage(imageFile);
            if (url == null)
                throw new FileNotFoundException();
            ChatServer server = new ChatServer();
            server.setAvatar(url);
            int update = this.chatServerService.updateServerById(server, serverId);
            return "{\"updates\":" + update + "}";
        } catch (Exception e) {
            resp.setStatus(500);
            return "{\"error\":\"" + e.getMessage() +"\"";
        }
    }

    @PostMapping(value = "/edit_server/update_banner")
    @ResponseBody
    public String updateBanner(@RequestPart("image") MultipartFile imageFile, @RequestPart("serverId") int serverId, @RequestPart("token") String token, HttpServletResponse resp) {
        Integer userId = userService.getUserByToken(token).getUserId();
        if ( userId == null )
            return "{message: \"invalid credential\"}";
        try {
            if ( chatServerService.getUserIdentityOfServer(serverId, userId).getIndex() < 1 )
                resp.setStatus(403);
            String url = this.fileService.uploadImage(imageFile);
            if (url == null)
                throw new FileNotFoundException();
            ChatServer server = new ChatServer();
            server.setBanner(url);
            int update = this.chatServerService.updateServerById(server, serverId);
            return "{\"updates\":" + update + "}";
        } catch (Exception e) {
            resp.setStatus(500);
            return "{\"error\":\"" + e.getMessage() +"\"";
        }
    }
    @GetMapping(value = "/invite_user", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUserToServer(@RequestParam("userId") Integer userId,
                                @RequestParam("serverId") Integer serverId,
                                HttpServletRequest req,
                                HttpServletResponse resp
                                ) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetUserIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if (
                selfIdentity.getIndex() > 0 && targetUserIdentity.getIndex() < 1
        )
        {   chatServerService.addUser(serverId, userId);
            return "{message: \"operation successful\"}";
        } else return "{message: \"you are not allowed to perform this operation\"}";
    }

    @GetMapping(value = "/banish_user", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String removeUserFromServer(@RequestParam("userId") Integer userId,
                                     @RequestParam("serverId") Integer serverId,
                                     HttpServletRequest req,
                                     HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity.getIndex() < targetIdentity.getIndex() )
            return "{message: \"you are not allowed to perform this operation\"}";
        else if ( selfIdentity == Identity.OWNER )
            return "{message: \"you are not allowed to perform this operation\"}";
        else chatServerService.removeUser(serverId, userId);
            return "{message: \"operation successful\"}";
    }

    @PostMapping(value = "/get_member", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMember(@RequestParam("userId") Integer userId, @RequestParam("serverId") Integer serverId) throws JsonProcessingException {
        ServerMember memberMap = chatServerService.getServerMemberById(serverId, userId);
        if ( memberMap != null )
            return objectMapper.writeValueAsString(memberMap);
        else {
            User user = userService.getUserById(userId);
            if ( user != null) {
                memberMap = ServerMember.nonMember(user);
            }
            else {
                memberMap = ServerMember.nonMember(User.ghost());
            }
        }
        return objectMapper.writeValueAsString(memberMap);
    }

    @PostMapping(value = "/get_member_map", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMemberMap(@RequestParam("serverId") int serverId) throws JsonProcessingException {
        List<ServerMember> memberMapList = chatServerService.getAllMembersInServer(serverId);
        return objectMapper.writeValueAsString(memberMapList);
    }

    @RequestMapping("/manage_server")
    public String manageServer(@RequestParam("serverId") Integer serverId, ModelMap modelMap, HttpServletRequest req) throws JsonProcessingException {
        User user = (User) req.getSession().getAttribute("user");
        Identity userIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        if ( userIdentity.getIndex() >= 0 )
            modelMap.addAttribute("permission", userIdentity.getIndex());
        ChatServer server = chatServerService.getServerById(serverId, false);
        System.out.println(server);
        System.out.println(objectMapper.writeValueAsString(server));
        modelMap.addAttribute("serverInfo", objectMapper.writeValueAsString(server));
        return "manage-server";
    }

    @GetMapping(value = "/server_info", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getServerInfo(@RequestParam("serverId") int serverId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(chatServerService.getServerById(serverId, false));
    }
    @GetMapping(value = "/list_users", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String toList(@RequestParam(name = "page", defaultValue = "1") @Nullable Integer page,
                         @RequestParam("searchText") @Nullable String searchText,
                         @RequestParam("serverId") Integer serverId) throws JsonProcessingException {
        PageInfo<ServerMember> serverMembers;
        if (searchText == null)
            serverMembers = chatServerService.getPagedUsers(page, serverId);
        else serverMembers = chatServerService.searchUserByText(page, searchText, serverId);
        return objectMapper.writeValueAsString(serverMembers);
    }

    @GetMapping(value = "/set_admin", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setAdmin(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity == Identity.OWNER && selfIdentity.getIndex() > targetIdentity.getIndex() ) {
            this.chatServerService.updateUserIdentity(serverId, userId, Identity.ADMIN, null);
            return "{message: \"operation successful\"}";
        }
        else
            return "{message: \"you are not allowed to perform this operation\"}";
    }

    @GetMapping(value = "/unset_admin", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String unsetAdmin(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity == Identity.OWNER && selfIdentity.getIndex() > targetIdentity.getIndex() ) {
            this.chatServerService.updateUserIdentity(serverId, userId, Identity.MEMBER, null);
            return "{message: \"operation successful\"}";
        }
        else
            return "{message: \"you are not allowed to perform this operation\"}";
    }

    @GetMapping(value = "/set_nickname", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setNickname(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, @RequestParam("nickname") String nickname, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity.getIndex() > 0 && user.getUserId().equals(userId) || selfIdentity.getIndex() > targetIdentity.getIndex() ) {
            this.chatServerService.updateUserIdentity(serverId, userId, null, nickname);
            return "{message: \"operation successful\"}";
        }
        else
            return "{message: \"you are not allowed to perform this operation\"}";
    }
}
