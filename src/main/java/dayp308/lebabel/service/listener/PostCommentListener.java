package dayp308.lebabel.service.listener;
import dayp308.lebabel.repository.jpa.CommentClosureRepository;
import dayp308.lebabel.repository.jpa.PostRepository;
import jakarta.transaction.Transactional;

import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import dayp308.lebabel.bean.entity.CommentClosure;
import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.bean.entity.post.Post;

@Component
public class PostCommentListener {

    private final PostRepository postRepository;

    private final CommentClosureRepository commentClosureRepository;

    PostCommentListener(CommentClosureRepository commentClosureRepository, PostRepository postRepository) {
        this.commentClosureRepository = commentClosureRepository;
        this.postRepository = postRepository;
    }

// https://stackoverflow.com/questions/44752567/save-data-in-a-method-of-eventlistener-or-transactionaleventlistener
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional
    public void beforeCommit(CommentCreateEvent event) {
        PostComment source = event.getSource();
        Post sourcePost = source.getPost();
        // 更新时间
        sourcePost.setLastUpdateTime(Instant.now());
        postRepository.save(sourcePost);
        // 插入闭包表
        long depth = 0;
        for (var ancestor = source; ancestor != null; ancestor = ancestor.getParentComment()) {
            CommentClosure closure = new CommentClosure();
            closure.setAncestor(ancestor);
            closure.setDescendant(source);
            closure.setDepth(depth++);
            commentClosureRepository.save(closure);
        }

    }
}
