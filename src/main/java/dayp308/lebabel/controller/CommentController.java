package dayp308.lebabel.controller;

import dayp308.lebabel.entity.comment.PostComment;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.view.comment.CommentView;
import dayp308.lebabel.repository.CommentRepository;
import dayp308.lebabel.repository.projection.CommentData;
import dayp308.lebabel.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/no_auth/comment")
public class CommentController {

    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentController(PostService postService, CommentRepository commentRepository) {
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/get_category_id")
    public Integer getCategoryId(@RequestParam("id") PostComment comment) {
        return comment.getPost().getCategory().getId();
    }


    @GetMapping("/get_detail")
    public CommentView getCommentDetail(
            @RequestParam("id") int id,
            Authentication auth
    ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        CommentView view = postService.getCommentDetailedView(id, user);
        return view;
    }

    @GetMapping("/get_single")
    public CommentView getSingleComment(
            @RequestParam("id") int id,
            Authentication auth
    ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        CommentView view = postService.getCommentBriefView(id, user);
        return view;
    }

    @GetMapping("/get_data")
    public CommentData getCommentData(@RequestParam("id") long id, Authentication auth) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();
        return commentRepository.getCommentDataById(id, user);
    }
}
