package dayp308.chatroom.converter.comment;

import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.view.comment.CommentBriefView;
import dayp308.chatroom.entity.view.comment.CommentView;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.specification.CommentSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CommentToBriefView implements Converter<PostComment, CommentBriefView> {
    private final CommentRepository commentRepository;
    private final CommentToView commentToView;

    @Autowired
    public CommentToBriefView(CommentRepository commentRepository, CommentToView commentToView) {
        this.commentRepository = commentRepository;
        this.commentToView = commentToView;
    }

    @Override
    public CommentBriefView convert(PostComment source) {
        CommentBriefView target = commentToView.convert(source).getBriefView();
        target.setChildren(getChildren(source));
        return target;
    }

    private Set<CommentView> getChildren(PostComment source) {
        Set<PostComment> sourceChildren = commentRepository.findAll(
                CommentSpecs.allSubCommentByComment(source, false), PageRequest.of(0, 5)).toSet();
        Set<CommentView> children = new LinkedHashSet<>();
        sourceChildren.forEach( c -> {
            CommentView comment = commentToView.convert(c);
            children.add(comment);
        });
        return children;
    }
}
