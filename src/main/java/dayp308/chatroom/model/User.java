package dayp308.chatroom.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.hibernate.dialect.MariaDBDialect;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
public class User implements Serializable {
    private Integer userId;
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String password;
    private String avatar;
    private String token;

	public static User ghost() {
		User ghostUser = new User();
		ghostUser.userId = 0;
		ghostUser.username = "ghost";
		return ghostUser;
	}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
