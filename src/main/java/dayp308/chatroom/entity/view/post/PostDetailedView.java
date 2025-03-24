package dayp308.chatroom.entity.view.post;

import dayp308.chatroom.entity.view.UserBriefView;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Data
@Component
public class PostDetailedView {
    private Long id;
    private UserBriefView userBriefView;
    private String title;
    private String content;
    private Integer categoryId;
    private Instant createTime;
    private Instant lastUpdateTime;
    private List<String> tags;
    private Long viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;
}
