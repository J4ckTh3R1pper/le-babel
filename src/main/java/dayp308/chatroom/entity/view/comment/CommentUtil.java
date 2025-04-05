package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.projection.CommentProjection;
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

    public CommentBriefView getCommentBriefView(CommentProjection source, @Nullable User user) {
        CommentBriefView result = conversionService.convert(source, CommentBriefView.class);
        Objects.requireNonNull(result).setChildren(getBriefChildren(result, user));
        return result;
    }

    public CommentDetailedView getCommentDetailedView(CommentProjection source, @Nullable User user) {
        CommentDetailedView result = conversionService.convert(source, CommentDetailedView.class);
        result.setChildren(getDetailedChildren(result, user));
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<CommentView> getBriefChildren(CommentView source, @Nullable User user) {
        List<CommentProjection> list = commentRepository.findAllProjByCommentId(source.getId(), user, 5);
        Object result = conversionService.convert(list,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentProjection.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentView.class))
        );
        return (List<CommentView>) result;
    }

    @SuppressWarnings("unchecked")
    private List<CommentDetailedView> getDetailedChildren(CommentView source, @Nullable User user) {
        List<CommentProjection> list = commentRepository.findAllProjByCommentId(source.getId(), user, 0);
        List<CommentDetailedView> result = (List<CommentDetailedView>) conversionService.convert(list,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentProjection.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentDetailedView.class))
        );
        if (result != null && !result.isEmpty())
            result.forEach(c -> {
                c.setChildren(getDetailedChildren(c, user));
            });
        return result;
    }
}
