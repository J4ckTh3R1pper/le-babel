package dayp308.lebabel.entity.view.post;

import lombok.Data;

@Data

public class PostMinimalView {
    private Long id;
    private Long userId;
    private String title;
    private Integer categoryId;
    private Long likeCount;
    private Long commentCount;
    private String thumbnail;
}
