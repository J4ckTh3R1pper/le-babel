package dayp308.chatroom.entity.view;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Data
@Component
public class PostBriefView {
    private Long id;
    private UserBriefView userBriefView;
    private String title;
    private String content;
    private Integer categoryId;
    private Instant createTime;
    private Instant lastUpdateTime;
    private List<String> tagList;
    private Long views;
    private Integer LikeCount;
    private Integer bookmarks;
    private Integer commentCount;
    private List<String> thumbnails;

}
