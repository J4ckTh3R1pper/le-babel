package dayp308.lebabel.bean.view;

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
