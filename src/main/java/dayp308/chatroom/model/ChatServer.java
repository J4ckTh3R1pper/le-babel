package dayp308.chatroom.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
public class ChatServer {
    private int id;
    private String name;
    private String avatar;
    private String description;
    private String banner;

    private List<ServerMember> memberList;
	private List<Channel> channelList;

    public List<ServerMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<ServerMember> memberList) {
        this.memberList = memberList;
    }

	public List<Channel> getChannelList() {
		return this.channelList;
	}
	
	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}

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
