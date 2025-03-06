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
    private List<String> tags;
    private Long views;
    private Long likes;
    private Long comments;
    private List<String> thumbnails;

}
