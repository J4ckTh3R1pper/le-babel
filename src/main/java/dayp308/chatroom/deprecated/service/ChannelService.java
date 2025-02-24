package dayp308.chatroom.deprecated.service;

import dayp308.chatroom.deprecated.model.Channel;
import dayp308.chatroom.deprecated.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChannelService implements IChannelService{

    private final ChannelMapper channelMapper;
    @Autowired
    public ChannelService(ChannelMapper channelMapper) {
        this.channelMapper = channelMapper;
    }
    @Override
    public List<Channel> getChannelList(int id) {
        return channelMapper.getChannelList(id);
    }

    @Override
    public Channel findChannelInServer(int channelId, int serverId) {
        return channelMapper.findChannelInServer(channelId, serverId);
    }

}
