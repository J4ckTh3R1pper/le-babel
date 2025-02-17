package dayp308.chatroom.controller;

import dayp308.chatroom.model.ChatServer;
import dayp308.chatroom.model.ServerFile;
import dayp308.chatroom.model.User;
import dayp308.chatroom.deprecated.ChannelService;
import dayp308.chatroom.deprecated.ChatServerService;
import dayp308.chatroom.deprecated.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


public class ViewController {
    private final ChannelService channelService;
    private final ChatServerService chatServerService;
    private final FileService fileService;

	private ObjectMapper objectMapper;
    public ViewController(ChatServerService chatServerService, ChannelService channelService, FileService fileService) {
        this.channelService = channelService;
		this.objectMapper = new ObjectMapper();
        this.chatServerService = chatServerService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/login")
    public ModelAndView loginView() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/chat")
    public String chatView(@RequestParam("serverId") String serverId , ModelMap modelMap, HttpServletRequest request) throws JsonProcessingException {
        int sid = Integer.parseInt(serverId);
        User user = (User) (request.getSession().getAttribute("user"));
        int identity = chatServerService.getUserIdentityOfServer(sid, user.getUserId()).getIndex();
        if ( identity < 0  ) return "index";

        ChatServer server = chatServerService.getServerById(sid, true);
        modelMap.addAttribute("permission", identity);
        modelMap.addAttribute("serverInfo", objectMapper.writeValueAsString(server));
        modelMap.addAttribute("serverId", sid);
        return "chat";
    }

    @RequestMapping(value = "/uploadtest")
    public String uploadView(@RequestParam("serverId") String serverId, ModelMap modelMap, HttpServletRequest req) throws JsonProcessingException {
        int sid = Integer.parseInt(serverId);
        User user = (User) (req.getSession().getAttribute("user"));
        if (chatServerService.getUserIdentityOfServer(sid, user.getUserId()).getIndex() < 0)
            return "index";
        List<ServerFile> serverFiles = fileService.getFilesInServer(sid);
        modelMap.addAttribute("filePage", objectMapper.writeValueAsString(serverFiles));
        modelMap.addAttribute("serverId", sid);
        return "uploadtest";
    }
}
