package dayp308.chatroom.mapper;

import java.util.List;

import dayp308.chatroom.bean.ServerMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dayp308.chatroom.bean.ChatServer;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChatServerMapper {
	public List<ChatServer> getServerByName(@Param("keyword") String keyword);
	public ChatServer getServerById(@Param("server_id") int id);
	public int createServer(@Param("server") ChatServer server, int ownerId);
	public void updateServerById(@Param("server") ChatServer server, @Param("server_id") int id);
	public boolean addUser(@Param("server_id") int id, @Param("user_id") int userId);
	public boolean removeUser(@Param("server_id") int id, @Param("user_id") int userId);
	public void updateUserIdentity(@Param("server_id") int id, @Param("user_id") int userId, @Param("identity") Identity identity, @Param("nickname") String nickname);

	public String getUserIdentityOfServer(@Param("server_id") int serverId, @Param("user_id") int userId);
	public String getUserNicknameOfServer(@Param("server_id") int serverId, @Param("user_id") int userId);

	List<ServerMember> getAllUsersInServer(@Param("server_id") int serverId);

	List<ServerMember> searchUserByTextInServer(@Param("server_id") int serverId, @Param("text") String text);

}
