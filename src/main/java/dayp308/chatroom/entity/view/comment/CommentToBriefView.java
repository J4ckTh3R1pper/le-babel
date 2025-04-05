package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.repository.projection.CommentProjection;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentToBriefView implements Converter<CommentProjection, CommentBriefView> {

    @Override
    public CommentBriefView convert(CommentProjection source) {
        CommentBriefView target = new CommentBriefView();
        BeanUtils.copyProperties(source, target);
        return target;
    }

}
