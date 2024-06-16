package dayp308.chatroom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dayp308.chatroom.bean.Channel;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.Message;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.ChannelService;
import dayp308.chatroom.service.ChatServerService;
import dayp308.chatroom.service.MessageService;
import dayp308.chatroom.service.UserService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
@ServerEndpoint(value = "/server/{serverId}/{Token}")
public class ChatServerEndpoint implements ApplicationContextAware {
    private int userId;
    private int serverId;

    private static ApplicationContext appContext;

    private UserService userService;
    private ChatServerService chatServerService;
    private MessageService messageService;
    private ChannelService channelService;

    private ObjectMapper objectMapper;

    static Set<Session> connections = Collections.synchronizedSet(new HashSet<>());

    private static class ServerCloseReason {
        protected static class Codes {
            protected static final CloseReason.CloseCode UNAUTHORIZED = CloseReason.CloseCodes.valueOf("4004");
            protected static final CloseReason.CloseCode NOT_ALLOWED = CloseReason.CloseCodes.valueOf("4005");
        }
        protected static final CloseReason UNAUTHORIZED = new CloseReason(Codes.UNAUTHORIZED, "Unauthorized");
        protected static final CloseReason NOT_ALLOWED = new CloseReason(Codes.NOT_ALLOWED, "Not allowed");

    }

    @OnOpen
    public void onOpen(Session session, @PathParam("Token") String param, @PathParam("serverId") String param1) throws IOException {
        this.userService = appContext.getBean(UserService.class);
        this.chatServerService = appContext.getBean(ChatServerService.class);

        User user = userService.getUserByToken(param);
        if (user != null) {
            userId = user.getUserId();
            serverId = Integer.parseInt(param1);
            Identity identity = chatServerService.getUserIdentityOfServer(serverId, userId);

            if ( identity != null)
                connections.add(session);
            else session.close(ServerCloseReason.NOT_ALLOWED);

        } else session.close(ServerCloseReason.UNAUTHORIZED);

        this.messageService = appContext.getBean(MessageService.class);
        this.channelService = appContext.getBean(ChannelService.class);
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );
        System.out.println("user " + userId + " connected to server " + serverId);
    }

    private void broadcast(String str) {
        for (Session client : connections) {
            send(str, client);
        }
    }

    private void send(String str, Session client) {
        try {
            client.getBasicRemote().sendText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String s, boolean b) throws JsonProcessingException {
        System.out.println(s);
        HashMap<String, String> json = objectMapper.readValue(s, new TypeReference<>() {
        });
        System.out.println(json);
        int channel = Integer.parseInt(json.get("channel"));
        Channel ch = channelService.findChannelInServer(channel, serverId);
        if ( ch == null )
            return;

        Message msg = messageService.insertMessage(
                new Message(
                    json.get("text"),
                    this.userId,
                    channel,
                    "",
                    Timestamp.from(Instant.now()),
                    0,
                        "user")
        );
//        System.out.println(msg);
//        String msgStr = String.format("{\"type\":\"chat\",\"text\":\"%s\",\"user\":\"%s\",\"date\":\"%s\" }", s, this.userId, new Date().getTime());
        String msgStr = objectMapper.writeValueAsString(msg);
        System.out.println(msgStr);
        broadcast(msgStr);
    }

    @OnError
    public void onError(Session session, Throwable cause) throws IOException {
        System.out.println(cause.getMessage());
        connections.remove(session);
        session.close();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        System.out.println("connection closed, reason:" + reason.getReasonPhrase());
        connections.remove(session);
        session.close();

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ChatServerEndpoint.appContext = applicationContext;
    }
}
