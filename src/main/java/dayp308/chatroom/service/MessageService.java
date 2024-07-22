package dayp308.chatroom.service;

import dayp308.chatroom.bean.Message;
import dayp308.chatroom.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageService{

    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public List<Message> getAllMessage() {
        return this.messageMapper.getAllMessage();
    }

    @Override
    public Message insertMessage(Message msg) {
       this.messageMapper.insertMessage(msg);
       System.out.println("from jdbc:" + msg.toString());
       return msg;
    }

    @Override
    public Message getMessageById(int index) {
        return this.messageMapper.getMessageById(index);
    }

    @Override
    public List<Message> getMessageInChannelByTime(int channel, long startTime, long endTime) {
        return this.messageMapper.getMessageInChannelByTime(channel, startTime / 1000, endTime / 1000);
    }

	@Override
	public List<Message> getLastMessageInChannel(int channelId, int index) {
		return this.messageMapper.getLastMessageInChannel(channelId, index);
	}

	@Override
	public List<Message> getLastMessageInChannelByTimestamp(int channelId, long timestamp) {
		return this.messageMapper.getLastMessageInChannelByTimestamp(channelId, timestamp);
	}

    @Override
    public void deleteMessageById(int index) {
        this.messageMapper.deleteMessageById(index);
    }
}
