package dayp308.lebabel.entity.view.comment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@Setter
@Component
public class CommentView implements Serializable {
    protected Long id;
    protected Long postId;
    protected Long userId;
    protected Integer categoryId;
    protected String commentBody;
    protected Instant createTime;
    protected Long likeCount;
    protected Boolean liked;
    protected Long parentCommentId;
    protected Long childCount;
    protected List<CommentView> children = new ArrayList<>();

    protected CommentView(){}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommentView that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void dfs(Consumer<CommentView> function) {
        this.children.forEach(child -> {
            function.accept(child);
            child.dfs(function);
        });
    }
}
