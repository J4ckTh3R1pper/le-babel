package dayp308.chatroom.deprecated.mapper;

import dayp308.chatroom.deprecated.model.Channel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelMapper {
    public List<Channel> getChannelList(@Param("server_id") int serverId);
    public Channel findChannelInServer(@Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void addUser(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void removeUser(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void changeIdentity(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public String getIdentity(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
}
