package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.repository.projection.CommentProjection;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentToView implements Converter<CommentProjection, CommentView> {

    @Override
    public CommentView convert(CommentProjection source) {
        CommentView target = new CommentView();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
