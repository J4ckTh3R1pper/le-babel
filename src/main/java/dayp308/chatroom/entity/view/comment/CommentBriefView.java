package dayp308.chatroom.entity.view.comment;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommentBriefView extends CommentView {
    private Set<CommentView> children = new LinkedHashSet<>();
    @Setter(AccessLevel.NONE)
    private int childCount;

    public void setChildren(Set<CommentView> children) {
        this.children = children;
        this.childCount = children.size();
    }
}
