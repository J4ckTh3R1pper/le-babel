package dayp308.chatroom.deprecated;

import java.util.List;
import dayp308.chatroom.model.*;

public interface IChannelService {
    public List<Channel> getChannelList(int id);
    public Channel findChannelInServer(int channelId, int serverId);
}
