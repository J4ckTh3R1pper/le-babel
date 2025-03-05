package dayp308.chatroom.entity.view;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Data
@Component
public class UserBriefView {
    private Long userId;
    private String nickName;
    private String headImgUrl;
    private String role;
    private Integer level;
    private String location;
    private String title;
}
