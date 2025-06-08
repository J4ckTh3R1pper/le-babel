package dayp308.lebabel.converter.comment;

import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.comment.PostComment;
import dayp308.lebabel.entity.business.CommentCreationForm;
import dayp308.lebabel.repository.CommentRepository;
import dayp308.lebabel.repository.PostRepository;
import dayp308.lebabel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToComment implements Converter<CommentCreationForm, PostComment> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public FormToComment(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostComment convert(CommentCreationForm source) {
        Post post = postRepository.findById(source.getPostId()).orElseThrow();
        PostComment target = new PostComment();
        target.setPost(post);
        target.setCommentBody(source.getCommentBody());
        if ( source.getParentCommentId() != null )
            target.setParentComment(commentRepository.findById(source.getParentCommentId()).orElse(null));
        return target;
    }
}
