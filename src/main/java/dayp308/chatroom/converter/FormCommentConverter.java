package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.dto.CommentDTO;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormCommentConverter implements Converter<CommentCreationForm, PostComment> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public FormCommentConverter(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostComment convert(CommentCreationForm source) {
        Post post = postRepository.findById(source.getPostId()).orElseThrow();
        User user = userRepository.findByToken(source.getToken()).orElseThrow();
        PostComment target = new PostComment();
        target.setPost(post);
        target.setCommentUser(user);
        target.setCommentBody(source.getCommentBody());
        target.setParentComment(commentRepository.findById(source.getParentCommentId()).orElse(null));
        return target;
    }
}
