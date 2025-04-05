package dayp308.chatroom.entity.view.comment;

import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
public class CommentBriefView extends CommentView {
    private List<CommentView> children = new ArrayList<>();
    private int childCount;

    protected CommentBriefView() {}

    public void setChildren(List<CommentView> children) {
        this.children = children;
        this.childCount = children.size();
    }
}
