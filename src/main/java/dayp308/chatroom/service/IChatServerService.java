package dayp308.chatroom.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.ServerMember;
import dayp308.chatroom.bean.User;

import java.util.List;

public interface IChatServerService {

    public List<ChatServer> getServerByName(String keyword);
    public ChatServer getServerById(int id);
    public void createServer(ChatServer server, int owner);
    public void updateServerById(ChatServer server, int id);
    public boolean addUser(int id, int user_id);
    public boolean removeUser(int id, int user_id);
    public void updateUserIdentity(int id, int user_id, Identity identity, String nickname);
    public PageInfo<ServerMember> getPagedUsers(Integer page, Integer serverId);
    public PageInfo<ServerMember> searchUserByText(Integer page, String text, Integer serverId);
	public List<ServerMember> getAllMembersInServer(int serverId);
}
