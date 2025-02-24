package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.dto.CommentDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentDTOConverter implements Converter<PostComment, CommentDTO> {

    @Override
    public CommentDTO convert(PostComment source) {
        CommentDTO target = new CommentDTO();
        BeanUtils.copyProperties(source, target);
        target.setPostId(source.getPost().getId());
        target.setUserId(source.getCommentUser().getId());
        return target;
    }
}
