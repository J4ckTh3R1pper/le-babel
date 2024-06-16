package dayp308.chatroom.service;

import dayp308.chatroom.bean.Channel;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
