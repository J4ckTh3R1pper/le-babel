package dayp308.chatroom.entity.view.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailedView {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer categoryId;
    private Instant createTime;
    private Instant lastUpdateTime;
    private List<String> tags;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Boolean liked;
}
