package dayp308.lebabel.controller.authenticated;

import dayp308.lebabel.bean.view.LikeResponse;
import dayp308.lebabel.bean.ao.PostForm;
import dayp308.lebabel.bean.entity.post.Post;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.repository.jpa.PostLikeRepository;
import dayp308.lebabel.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/post/")
public class AuthenticatedPostController {

    private final PostService postService;
    private final PostLikeRepository postLikeRepository;

    public AuthenticatedPostController(PostService postService, PostLikeRepository postLikeRepository) {
        this.postService = postService;
        this.postLikeRepository = postLikeRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostForm postForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addPost(postForm, ( (User) auth.getPrincipal() )).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PreAuthorize(
            "@authz.isPostOwner(#post) or @authz.hasAuthorityGe(#post, 'MODERATOR')")
    @PutMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestParam("id") @P("post") Post post) {
        postService.deletePost(post);
        return new ResponseEntity<>("deleted thread: " + post.getId(), HttpStatus.OK);
    }

    @PutMapping("/like")
    public LikeResponse likePost(@RequestParam("id") Post post, Authentication auth) throws EntityNotFoundException {
        Boolean liked = postService.likePost(post, ( (User) auth.getPrincipal() ));
        return new LikeResponse(
                postLikeRepository.countByPostId(post.getId()),
                liked
        );
    }

}
