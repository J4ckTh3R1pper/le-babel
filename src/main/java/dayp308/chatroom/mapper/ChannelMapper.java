package dayp308.chatroom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import dayp308.chatroom.bean.*;
import java.util.List;

@Mapper
@Repository
public interface ChannelMapper {
    public List<Channel> getChannelList(@Param("server_id") int serverId);
    public Channel findChannelInServer(@Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void addUser(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void removeUser(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public void changeIdentity(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
    // public String getIdentity(@Param("user_id") int userId, @Param("channel_id") int channelId, @Param("server_id") int serverId);
}
