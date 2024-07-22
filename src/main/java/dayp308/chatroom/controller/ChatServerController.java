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
		Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetUserIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
		if (
				selfIdentity.getIndex() > 0 && targetUserIdentity.getIndex() < 1

				)
			chatServerService.addUser(serverId, userId);
        resp.sendRedirect("/list_users?serverId=" + serverId);
    }

    @RequestMapping(value = "/banish_user")
    public void removeUserFromServer(@RequestParam("userId") Integer userId,
                                     @RequestParam("serverId") Integer serverId,
                                     HttpServletRequest req,
                                     HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
        if ( selfIdentity.getIndex() < targetIdentity.getIndex() )
            resp.sendRedirect("/list_users?serverId=" + serverId);
        else if ( selfIdentity == Identity.OWNER )
            resp.sendRedirect("/list_users?serverId=" + serverId);
        else chatServerService.removeUser(serverId, userId);
        resp.sendRedirect("/list_users?serverId=" + serverId);
    }

    @PostMapping(value = "/get_username", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUname(@RequestParam("userId") Integer userId, @RequestParam("serverId") Integer serverId) {
		ServerMember memberMap = chatServerService.getServerMemberById(serverId, userId);
        if ( memberMap != null )
            return "{\"userId\":" + memberMap.getUser().getUserId() +
		    	",\"nickname\":\"" + ( memberMap.getNickname() == null || memberMap.getNickname().isBlank() ? memberMap.getUser().getUsername() : memberMap.getNickname() ) +
			    "\",\"avatar\":\"" + memberMap.getUser().getAvatar() +
			    "\",\"identity\":\"" + memberMap.getIdentity().getName() + "\"}";
        else {
            User user = userService.getUserById(userId);
            if ( user != null)
                return "{\"userId\":" + user.getUserId() +
                        ",\"nickname\":\"" + user.getUsername() +
                        "\",\"avatar\":\"" + user.getAvatar() +
                        "\",\"identity\":\"" + Identity.NON_MEMBER.getName() + "\"}";
            else return "{\"userId\":" + 0 +
                    ",\"nickname\":\"" + "ghost" +
                    "\",\"avatar\":" + "null" +
                    ",\"identity\":\"" + Identity.NON_MEMBER.getName() + "\"}";
        }
    }

    @RequestMapping(value = "/list_users")
    public String toList(@RequestParam(name = "page", defaultValue = "1") @Nullable Integer page,
                         @RequestParam("searchText") @Nullable String searchText,
                         @RequestParam("serverId") Integer serverId,
                         ModelMap modelMap,
                         HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
		Identity userIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
		if ( userIdentity.getIndex() >= 0 )
		    modelMap.addAttribute("permission", userIdentity.getIndex());

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

	@RequestMapping(value = "/set_admin")
	public void setAdmin(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
		if ( selfIdentity == Identity.OWNER && selfIdentity.getIndex() > targetIdentity.getIndex() ) {
			this.chatServerService.updateUserIdentity(serverId, userId, Identity.ADMIN, null);
		}
		
		resp.sendRedirect("/list_users?serverId=" + serverId);
	}

	@RequestMapping(value = "/unset_admin")
	public void unsetAdmin(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
		if ( selfIdentity == Identity.OWNER && selfIdentity.getIndex() > targetIdentity.getIndex() ) {
			this.chatServerService.updateUserIdentity(serverId, userId, Identity.MEMBER, null);
		}
		
		resp.sendRedirect("/list_users?serverId=" + serverId);
	
	}

	@RequestMapping(value = "/set_nickname")
	public void setNickname(@RequestParam("serverId") Integer serverId, @RequestParam("userId") Integer userId, @RequestParam("nickname") String nickname, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Identity selfIdentity = chatServerService.getUserIdentityOfServer(serverId, user.getUserId());
        Identity targetIdentity = chatServerService.getUserIdentityOfServer(serverId, userId);
		if ( selfIdentity.getIndex() > 0 && user.getUserId().equals(userId) || selfIdentity.getIndex() > targetIdentity.getIndex() ) {
			this.chatServerService.updateUserIdentity(serverId, userId, null, nickname);
		}
		
		resp.sendRedirect("/list_users?serverId=" + serverId);
	
	}
}
