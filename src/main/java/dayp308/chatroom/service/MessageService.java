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
       msg.setInd(this.messageMapper.insertMessage(msg));
       return msg;
    }

    @Override
    public Message getMessageById(int index) {
        return this.messageMapper.getMessageById(index);
    }

    @Override
    public List<Message> getMessageInChannelByTime(int channel, long timestamp) {
        timestamp = timestamp / 1000;
        return this.messageMapper.getMessageInChannelByTime(channel, timestamp - 1800, timestamp);
    }

    @Override
    public void deleteMessageById(int index) {
        this.messageMapper.deleteMessageById(index);
    }
}
