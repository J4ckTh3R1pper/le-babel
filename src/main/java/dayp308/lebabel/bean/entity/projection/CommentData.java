package dayp308.lebabel.bean.entity.projection;

import lombok.Value;

@Value
public class CommentData {
    private Long childCount;
    private Boolean liked;
    private Long likeCount;
}
