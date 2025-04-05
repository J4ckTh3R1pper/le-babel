package dayp308.chatroom.entity.view;

import lombok.Data;

@Data
public class UserBriefView {
    private Long id;
    private String nickName;
    private String headImgUrl;
    private Integer role;
    private Integer level;
    private String location;
    private String title;
}
