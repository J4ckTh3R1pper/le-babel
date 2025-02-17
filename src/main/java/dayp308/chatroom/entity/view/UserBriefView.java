package dayp308.chatroom.entity.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserBriefView {
    private Long id;
    private String nickName;
    private String headImgUrl;
    private byte role;
    private String experience;
    private String location;
    private String title;
}
