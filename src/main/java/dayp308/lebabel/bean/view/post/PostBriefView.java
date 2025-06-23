package dayp308.lebabel.bean.view.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostBriefView {
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
    private List<String> thumbnails;
    private Boolean liked;
}
