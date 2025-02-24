package dayp308.chatroom.converter;

import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.dto.CommentDTO;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter implements Converter<CommentDTO, PostComment> {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentConverter(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public PostComment convert(CommentDTO source) {
        PostComment target = new PostComment();
        target.setCommentUser(userRepository.getReferenceById(source.getUserId()));
        target.setPost(postRepository.getReferenceById(source.getPostId()));
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
