package dayp308.chatroom.deprecated.service;

import dayp308.chatroom.deprecated.model.Message;

import java.util.List;

public interface IMessageService {
    public Message insertMessage(Message msg);
    public Message getMessageById(int index);
    public List<Message> getMessageInChannelByTime(int channel, long startTime, long endTime);
    public void deleteMessageById(int index);
	public List<Message> getLastMessageInChannel(int channelId, int index);
	public List<Message> getLastMessageInChannelByTimestamp(int channelId, long timestamp);
}
