package dayp308.chatroom.deprecated.service;

import com.github.pagehelper.PageInfo;
import dayp308.chatroom.deprecated.model.ChatServer;
import dayp308.chatroom.deprecated.model.Identity;
import dayp308.chatroom.deprecated.model.ServerMember;

import java.util.List;

public interface IChatServerService {

    public List<ChatServer> getServerByName(String keyword);
    public ChatServer getServerById(int id, boolean isFull);
    public void createServer(ChatServer server, int owner);
    public int updateServerById(ChatServer server, int id);
    public boolean addUser(int id, int user_id);
    public boolean removeUser(int id, int user_id);
    public void updateUserIdentity(int id, int user_id, Identity identity, String nickname);
    public PageInfo<ServerMember> getPagedUsers(Integer page, Integer serverId);
    public PageInfo<ServerMember> searchUserByText(Integer page, String text, Integer serverId);
	public List<ServerMember> getAllMembersInServer(int serverId);
}
