package dayp308.chatroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.pagehelper.PageInfo;
import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Message;
import dayp308.chatroom.bean.ServerMember;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.configuration.ChatServerEndpointExporter;
import dayp308.chatroom.service.ChatServerService;
import dayp308.chatroom.service.MessageService;
import dayp308.chatroom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {ChatServerEndpointExporter.class})})
class ChatSpringbootApplicationTests {

    private final ChatServerService chatServerService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageService messageService;

    private final UserService userService;

    @Autowired
    public ChatSpringbootApplicationTests(ChatServerService chatServerService, UserService userService, MessageService messageService) {
        this.chatServerService = chatServerService;
        this.messageService = messageService;
        this.userService = userService;
    }
    @Test
    void testChatServer() throws JsonProcessingException {
        System.out.println(this.objectMapper.writeValueAsString(this.chatServerService.getServerById(1, true)));
    }
    @Test
    void addServer() {
        ChatServer newServer = new ChatServer();
        newServer.setName("testServer");
        newServer.setDescription("testDescription");
        newServer.setAvatar("testAvatar");
        newServer.setBanner("testBanner");
        this.chatServerService.createServer(newServer, 11);
    }

    @Test
    void testMessageToJson() throws JsonProcessingException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Message> listMsg = messageService.getMessageInChannelByTime(2, format.parse("2023-07-28 15:05:42").getTime(), format.parse("2023-07-28 00:00:42").getTime());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listMsg);
        System.out.println(listMsg);
        System.out.println(str);
    }

    @Test
    void testLogin() {
        User user = userService.getUserLogin("m1911", "12345678");
        System.out.println(user);
    }


    @Test
    void testIdentity() {
        System.out.println(this.chatServerService.getUserIdentityOfServer(1, 4));
    }

    @Test
    void testListUser() {
        PageInfo<ServerMember> userPageInfo = chatServerService.getPagedUsers(2, 1);
            for ( ServerMember u : userPageInfo.getList() ) {
                System.out.println(u);
            }
    }

    @Test
    void testListUserSearchByName() {
        PageInfo<ServerMember> userPageInfo = chatServerService.searchUserByText(1, "i" ,1);
        for ( ServerMember u : userPageInfo.getList() ) {
            System.out.println(u);
        }

    }
}
