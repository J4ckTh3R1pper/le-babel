package dayp308.chatroom.entity.view;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Data
@Component
public class PostUserView {
    private Long id;
    private UserBriefView userBriefView;
    private String postTitle;
    private String postContent;
    private Integer postCategoryId;
    private Instant createTime;
    private Instant lastUpdateTime;
    private List<String> tags;
    private Long postViews;
    private Long likes;
    private Long comments;
    private Boolean liked;

}
