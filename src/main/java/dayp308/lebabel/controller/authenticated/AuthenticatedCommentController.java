package dayp308.lebabel.controller.authenticated;

import dayp308.lebabel.bean.ao.CommentCreationForm;
import dayp308.lebabel.bean.view.LikeResponse;
import dayp308.lebabel.bean.entity.comment.PostComment;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.repository.jpa.CommentLikeRepository;
import dayp308.lebabel.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/comment")
public class AuthenticatedCommentController {

    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;

    public AuthenticatedCommentController(PostService postService, CommentLikeRepository commentLikeRepository) {
        this.postService = postService;
        this.commentLikeRepository = commentLikeRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createComment(@Valid @RequestBody CommentCreationForm commentCreationForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addComment(commentCreationForm, ((User) auth.getPrincipal()) ).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PreAuthorize(
            "@authz.isCommentOwner(#comment) or @authz.hasAuthorityGe(#comment, 'MODERATOR')")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("id") PostComment comment) {
        postService.deleteComment(comment);
        return new ResponseEntity<>("deleted comment: " + comment.getId(), HttpStatus.OK);
    }

    @PutMapping("/like")
    public LikeResponse likeComment(@RequestParam("id") PostComment comment, Authentication auth) throws EntityNotFoundException {
        Boolean liked = postService.likeComment(comment, ( (User) auth.getPrincipal() ));
        return new LikeResponse(
                commentLikeRepository.countByCommentId(comment.getId()),
                liked
        );
    }

}
