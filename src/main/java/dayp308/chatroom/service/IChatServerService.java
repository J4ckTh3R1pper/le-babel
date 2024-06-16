package dayp308.chatroom.service;

import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Identity;

import java.util.List;

public interface IChatServerService {

    public List<ChatServer> getServerByName(String keyword);
    public ChatServer getServerById(int id);
    public void createServer(ChatServer server, int owner);
    public void updateServerById(ChatServer server, int id);
    public boolean addUser(int id, int user_id);
    public boolean removeUser(int id, int user_id);
    public void updateUserIdentity(int id, int user_id, Identity identity);
}
