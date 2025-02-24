package dayp308.chatroom.deprecated.model;

import org.springframework.stereotype.Component;

@Component
public class Channel {
    private String channelName;
    private int channelId;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "{name:\"" + this.channelName + "\", id:" + this.channelId + "}";
    }
}
