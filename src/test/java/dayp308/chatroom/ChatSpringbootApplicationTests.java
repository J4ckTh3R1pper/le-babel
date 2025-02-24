package dayp308.chatroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.pagehelper.PageInfo;
import dayp308.chatroom.deprecated.model.ChatServer;
import dayp308.chatroom.deprecated.model.Message;
import dayp308.chatroom.deprecated.model.ServerMember;
import dayp308.chatroom.deprecated.model.User;
import dayp308.chatroom.configuration.ChatServerEndpointExporter;
import dayp308.chatroom.deprecated.service.ChatServerService;
import dayp308.chatroom.deprecated.service.MessageService;
import dayp308.chatroom.deprecated.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {ChatServerEndpointExporter.class})})
class ChatSpringbootApplicationTests {

    private final ChatServerService chatServerService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageService messageService;

    private final UserService userService;

    public ChatSpringbootApplicationTests(ChatServerService chatServerService, UserService userService, MessageService messageService) {
        this.chatServerService = chatServerService;
        this.messageService = messageService;
        this.userService = userService;
    }
    void testChatServer() throws JsonProcessingException {
        System.out.println(this.objectMapper.writeValueAsString(this.chatServerService.getServerById(1, true)));
    }
    void addServer() {
        ChatServer newServer = new ChatServer();
        newServer.setName("testServer");
        newServer.setDescription("testDescription");
        newServer.setAvatar("testAvatar");
        newServer.setBanner("testBanner");
        this.chatServerService.createServer(newServer, 11);
    }

    void testMessageToJson() throws JsonProcessingException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Message> listMsg = messageService.getMessageInChannelByTime(2, format.parse("2023-07-28 15:05:42").getTime(), format.parse("2023-07-28 00:00:42").getTime());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listMsg);
        System.out.println(listMsg);
        System.out.println(str);
    }

    void testLogin() {
        User user = userService.getUserLogin("m1911", "12345678");
        System.out.println(user);
    }


    void testIdentity() {
        System.out.println(this.chatServerService.getUserIdentityOfServer(1, 4));
    }

    void testListUser() {
        PageInfo<ServerMember> userPageInfo = chatServerService.getPagedUsers(2, 1);
            for ( ServerMember u : userPageInfo.getList() ) {
                System.out.println(u);
            }
    }

    void testListUserSearchByName() {
        PageInfo<ServerMember> userPageInfo = chatServerService.searchUserByText(1, "i" ,1);
        for ( ServerMember u : userPageInfo.getList() ) {
            System.out.println(u);
        }

    }
}
