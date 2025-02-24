package dayp308.chatroom.deprecated.model;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;
@Component
public class ServerMember implements Serializable {
	@JsonUnwrapped
    private User user;
    private Identity identity;
    private String nickname;

		
	public static ServerMember nonMember(User user) {
		ServerMember member = new ServerMember();
		member.user = user;
		member.identity = Identity.NON_MEMBER;
		return member;
	}
    public ServerMember() {
    }

    public ServerMember(String identityStr, String nickname) {
        this.identity = Identity.getIdentity(identityStr);
        this.nickname = nickname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":" + user.getUserId() +
                ", \"username\":\"" + user.getUsername() +
                "\", \"avatar\":\"" + user.getAvatar() +
                "\", \"identity\":\"" + identity.getName() +
                "\", \"nickname\":\"" + nickname + "\"" +
                "}";
    }
}
