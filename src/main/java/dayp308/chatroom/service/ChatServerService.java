package dayp308.chatroom.service;

import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.mapper.ChatServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServerService implements IChatServerService{

    private final ChatServerMapper chatServerMapper;

    @Autowired
    public ChatServerService(ChatServerMapper chatServerMapper) { this.chatServerMapper = chatServerMapper; }

    @Override
    public List<ChatServer> getServerByName(String keyword) {
        return this.chatServerMapper.getServerByName(keyword);
    }

    @Override
    public ChatServer getServerById(int id) {
        return this.chatServerMapper.getServerById(id);
    }

    @Override
    public void createServer(ChatServer server, int ownerId) {
        this.chatServerMapper.createServer(server, ownerId);
        this.chatServerMapper.addUser(server.getId(), ownerId);
        this.chatServerMapper.updateUserIdentity(server.getId(), ownerId, Identity.OWNER);
    }

    @Override
    public void updateServerById(ChatServer server, int id) {
        this.chatServerMapper.updateServerById(server, id);
    }

    @Override
    public boolean addUser(int id, int user_id) {
        return this.chatServerMapper.addUser(id, user_id);
    }

    @Override
    public boolean removeUser(int id, int user_id) {
        return this.chatServerMapper.removeUser(id, user_id);
    }

    @Override
    public void updateUserIdentity(int id, int user_id, Identity identity) {
        if (identity != Identity.OWNER)
            this.chatServerMapper.updateUserIdentity(id, user_id, identity);
    }
    public Identity getUserIdentityOfServer(int serverId, int userId) {
        String identityStr = this.chatServerMapper.getUserIdentityOfServer(serverId, userId);
        return Identity.getIdentity(identityStr);
    }

    public String getUserNicknameOfServer(int serverId, User user) {
        String nickname = this.chatServerMapper.getUserNicknameOfServer(serverId, user.getUserId());
        if ( nickname == null || nickname.equals("") )
            return user.getUsername();
        return nickname;
    }
}
