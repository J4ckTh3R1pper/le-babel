package dayp308.chatroom.entity.view.comment;

import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.projection.CommentProjection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class CommentToDetailedView implements Converter<CommentProjection, CommentDetailedView> {

    @Override
    public CommentDetailedView convert(CommentProjection source) {
        CommentDetailedView target = new CommentDetailedView();
        BeanUtils.copyProperties(source, target);
        return target;
    }

}
