package dayp308.chatroom.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class ServerMember implements Serializable {
    private User user;
    private Identity identity;
    private String nickname;

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
        return "ServerMember{" +
                "user=" + user.getUserId() +
                ", identity=" + identity +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
