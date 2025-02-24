package dayp308.chatroom.deprecated.mapper;

import dayp308.chatroom.deprecated.model.Message;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    public List<Message> getAllMessage();
    @Options(useGeneratedKeys = true, keyProperty = "ind", keyColumn = "ind")
    public int insertMessage(@Param("msg") Message msg);
    public Message getMessageById(@Param("ind") int index);
    public List<Message> getMessageInChannelByTime(@Param("channel_id") int channel, @Param("start_time") long start_time, @Param("end_time") long end_time);
	public List<Message> getLastMessageInChannel(@Param("channel_id") int channelId, @Param("index") int index);
	public List<Message> getLastMessageInChannelByTimestamp(@Param("channel_id") int channelId, @Param("timestamp") long timestamp);
    public void deleteMessageById(@Param("ind") int index);

}
