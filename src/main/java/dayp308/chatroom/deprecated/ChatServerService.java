package dayp308.chatroom.deprecated;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dayp308.chatroom.model.ChatServer;
import dayp308.chatroom.model.Identity;
import dayp308.chatroom.model.ServerMember;
import dayp308.chatroom.model.User;
import dayp308.chatroom.deprecated.mapper.ChatServerMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChatServerService implements IChatServerService{

    private final ChatServerMapper chatServerMapper;

    @Autowired
    public ChatServerService(ChatServerMapper chatServerMapper) { this.chatServerMapper = chatServerMapper; }

    @Override
    public List<ChatServer> getServerByName(String keyword) {
        return this.chatServerMapper.getServerByName(keyword);
    }

    @Override
    public ChatServer getServerById(int id, boolean isFull) {
        if ( isFull )
            return this.chatServerMapper.getFullServerById(id);
        else
            return this.chatServerMapper.getServerById(id);
    }

    @Override
    public void createServer(ChatServer server, int ownerId) {
        this.chatServerMapper.createServer(server, ownerId);
        this.chatServerMapper.addUser(server.getId(), ownerId);
        this.chatServerMapper.updateUserIdentity(server.getId(), ownerId, Identity.OWNER, "");
    }

    @Override
    public int updateServerById(ChatServer server, int id) {
        return this.chatServerMapper.updateServerById(server, id);
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
    public void updateUserIdentity(int id, int user_id, Identity identity, String nickname) {
        if (identity != Identity.OWNER)
            this.chatServerMapper.updateUserIdentity(id, user_id, identity, nickname);
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

	public ServerMember getServerMemberById(int serverId, int userId) {
		return this.chatServerMapper.getServerMemberById(serverId, userId);
	}

    @Override
    public PageInfo<ServerMember> searchUserByText(Integer page, String text, Integer serverId) {
        PageHelper.startPage(page, 5);
        List<ServerMember> users = chatServerMapper.searchUserByTextInServer(serverId, text);
        PageInfo<ServerMember> pageInfo = new PageInfo<>(users);
        PageHelper.clearPage();
        return pageInfo;
    }
    @Override
    public PageInfo<ServerMember> getPagedUsers(Integer page, Integer serverId) {
        PageHelper.startPage(page, 5);
        List<ServerMember> list = chatServerMapper.getAllUsersInServer(serverId);
        PageInfo<ServerMember> pageInfo = new PageInfo<>(list);
        PageHelper.clearPage();
        return pageInfo;
    }
	@Override
	public List<ServerMember> getAllMembersInServer(int serverId) {
		List<ServerMember> list = chatServerMapper.getAllUsersInServer(serverId);
		return list;
	}
}
