package dayp308.chatroom.repository.listener;
import dayp308.chatroom.repository.CommentClosureRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import dayp308.chatroom.entity.CommentClosure;
import dayp308.chatroom.entity.comment.PostComment;

@Component
public class PostCommentListener {

    private final CommentClosureRepository commentClosureRepository;

    PostCommentListener(CommentClosureRepository commentClosureRepository) {
        this.commentClosureRepository = commentClosureRepository;
    }

// https://stackoverflow.com/questions/44752567/save-data-in-a-method-of-eventlistener-or-transactionaleventlistener
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional
    public void afterCommentCommit(CommentCreateEvent event) {
        long depth = 0;
        for (var ancestor = event.getSource(); ancestor != null; ancestor = ancestor.getParentComment()) {
            CommentClosure closure = new CommentClosure();
            closure.setAncestor(ancestor);
            closure.setDescendant(event.getSource());
            closure.setDepth(depth++);
            commentClosureRepository.save(closure);
        }

    }
}
