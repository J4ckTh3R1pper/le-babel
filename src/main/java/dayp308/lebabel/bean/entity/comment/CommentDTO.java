package dayp308.lebabel.bean.entity.comment;

import java.time.Instant;

public interface CommentDTO {
    Long getId();
    Post getPost();
    User getUser();
    String getCommentBody();
    Instant getCreateTime();
    CommentDTO getParentComment();
    Boolean getIsDeleted();

    interface Post {
        Long getId();
    }
    interface User {
        Long getId();
    }

}
