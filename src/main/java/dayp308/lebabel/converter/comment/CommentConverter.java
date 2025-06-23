package dayp308.lebabel.converter.comment;

import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.bean.entity.comment.CommentDTO;
import dayp308.lebabel.repository.jpa.PostRepository;
import dayp308.lebabel.repository.jpa.UserRepository;
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
        target.setUser(userRepository.getReferenceById(source.getUser().getId()));
        target.setPost(postRepository.getReferenceById(source.getPost().getId()));
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
