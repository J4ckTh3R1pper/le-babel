package dayp308.chatroom.service;

import java.util.List;
import dayp308.chatroom.bean.*;

public interface IChannelService {
    public List<Channel> getChannelList(int id);
    public Channel findChannelInServer(int channelId, int serverId);
}
