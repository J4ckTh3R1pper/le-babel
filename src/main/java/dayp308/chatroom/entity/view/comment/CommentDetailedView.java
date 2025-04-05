package dayp308.chatroom.entity.view.comment;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Getter
public class CommentDetailedView extends CommentView {
    private List<CommentDetailedView> children =  new ArrayList<>();
    private int childCount;
    @Setter
    private Long parentCommentId;

    protected CommentDetailedView() {}

    public void setChildren(List<CommentDetailedView> children) {
        this.children = children;
        this.childCount = countChild(children);
    }

    protected int countChild(List<CommentDetailedView> children) {
        AtomicInteger i = new AtomicInteger(0);
        if (children != null && !children.isEmpty()) {
            i.getAndAdd(children.size());
            children.forEach(c ->  {
                i.getAndAdd(c.getChildCount());
            });
        }
        return i.get();
    }

    public void dfs(Consumer<CommentDetailedView> function) {
        this.children.forEach(child -> {
            function.accept(child);
            child.dfs(function);
        });
    }
}
