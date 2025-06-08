package dayp308.lebabel.entity.view.comment;

import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.repository.CommentRepository;
import dayp308.lebabel.repository.projection.CommentProjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CommentUtil {
    private final CommentRepository commentRepository;
    private final ConversionService conversionService;

    @Autowired
    public CommentUtil(CommentRepository commentRepository, ConversionService conversionService) {
        this.commentRepository = commentRepository;
        this.conversionService = conversionService;
    }

    public CommentView getCommentBriefView(CommentProjection source, @Nullable User user) {
        CommentView result = Objects.requireNonNull(conversionService.convert(source, CommentView.class));
        setBriefChildren(result, user);
        return result;
    }

    public CommentView getCommentDetailedView(CommentProjection source, @Nullable User user) {
        CommentView result = conversionService.convert(source, CommentView.class);
        setDetailedChildren(result, user);
        return result;
    }

    private void buildChildrenTree(CommentView source, List<CommentView> closureList) {
        var directChildren = closureList.stream().filter(c -> c.getParentCommentId() == source.getId()).toList();
        closureList.removeAll(directChildren);
        directChildren.forEach(c -> {
            buildChildrenTree(c, closureList);
        });
        source.setChildren(directChildren);
    }

    @SuppressWarnings("unchecked")
    private void setBriefChildren(CommentView source, @Nullable User user) {
        List<CommentProjection> list = commentRepository.findClosureListByCommentId(source.getId(), user, 5, true);
        List<CommentView> result = (List<CommentView>) conversionService.convert(list,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentProjection.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentView.class))
        );
        buildChildrenTree(source, result);
    }

    @SuppressWarnings("unchecked")
    private void setDetailedChildren(CommentView source, @Nullable User user) {
        List<CommentProjection> list = commentRepository.findClosureListByCommentId(source.getId(), user, 0, true);
        List<CommentView> result = (List<CommentView>) conversionService.convert(list,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentProjection.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentView.class))
        );
        buildChildrenTree(source, result);
    }
}
