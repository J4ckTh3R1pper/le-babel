package dayp308.chatroom.deprecated.service;

import java.util.List;

import dayp308.chatroom.deprecated.model.Channel;

public interface IChannelService {
    public List<Channel> getChannelList(int id);
    public Channel findChannelInServer(int channelId, int serverId);
}
