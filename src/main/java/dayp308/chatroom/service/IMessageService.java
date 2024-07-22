package dayp308.chatroom.service;

import dayp308.chatroom.bean.Message;

import java.util.List;

public interface IMessageService {
    public Message insertMessage(Message msg);
    public Message getMessageById(int index);
    public List<Message> getMessageInChannelByTime(int channel, long startTime, long endTime);
    public void deleteMessageById(int index);
	public List<Message> getLastMessageInChannel(int channelId, long timestamp);
}
