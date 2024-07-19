package dayp308.chatroom.controller;

import dayp308.chatroom.bean.Channel;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.ChannelService;
import dayp308.chatroom.service.ChatServerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class ViewController {
    private final ChannelService channelService;
    private final ChatServerService chatServerService;
    @Autowired
    public ViewController(ChatServerService chatServerService, ChannelService channelService) {
        this.channelService = channelService;
        this.chatServerService = chatServerService;
    }

    @RequestMapping(value = "/login")
    public ModelAndView loginView() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/chat")
    public String chatView(@RequestParam("serverId") String serverId , ModelMap modelMap, HttpServletRequest request) {
        int sid = Integer.parseInt(serverId);
        User user = (User) (request.getSession().getAttribute("user"));
        if ( chatServerService.getUserIdentityOfServer(sid, user.getUserId()).getIndex() < 0  ) {
            return "index";
        }

        List<Channel> channelList = channelService.getChannelList(sid);
        modelMap.addAttribute("channelList", channelList);
        modelMap.addAttribute("serverId", sid);
        return "chat";
    }
}
