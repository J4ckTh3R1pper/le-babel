package dayp308.chatroom.bean;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ChatServer {
    private int id;
    private String name;
    private String avatar;
    private String description;
    private String banner;
	
	private HashMap<Integer, Identity> memberMap;
	private List<Channel> channelList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
