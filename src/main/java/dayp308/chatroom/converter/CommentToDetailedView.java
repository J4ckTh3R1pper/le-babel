package dayp308.chatroom.converter;

import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.view.comment.CommentDetailedView;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.specification.CustomSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;


@Component
public class CommentToDetailedView implements Converter<PostComment, CommentDetailedView> {
    private final CommentRepository commentRepository;
    private final CommentToView commentToView;

    @Autowired
    public CommentToDetailedView(CommentRepository commentRepository, CommentToView commentToView) {
        this.commentRepository = commentRepository;
        this.commentToView = commentToView;
    }

    @Override
    public CommentDetailedView convert(PostComment source) {
        CommentDetailedView target = commentToView.convert(source).getDetailedView();
        target.setChildren(getChildren(source));
        return target;
    }

    protected Set<CommentDetailedView> getChildren(PostComment source) {
        Set<PostComment> sourceChildren = commentRepository.findAll(CustomSpecifications.allSubCommentByComment(source));
        Set<CommentDetailedView> children = new LinkedHashSet<>();
        sourceChildren.forEach(c -> {
            CommentDetailedView child = commentToView.convert(c).getDetailedView();
            child.setChildren(getChildren(c));
            children.add(child);
        });
        return children;
    }
}
