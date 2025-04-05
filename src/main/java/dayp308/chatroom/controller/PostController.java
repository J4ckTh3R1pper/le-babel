package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.enums.PostOrderType;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.post.Post_;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.view.comment.CommentDetailedView;
import dayp308.chatroom.entity.view.post.PostBriefView;
import dayp308.chatroom.entity.view.post.PostDetailedView;
import dayp308.chatroom.entity.view.comment.CommentBriefView;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostService postService, UserService userService, UserRepository userRepository, ObjectMapper jacksonObjectMapper, PostRepository postRepository) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.postRepository = postRepository;
    }

    @PostMapping("/api/post/create")
    public ResponseEntity<Long> createPost(@Valid PostForm postForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addPost(postForm, ( (User) auth.getPrincipal() )).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping("/api/comment/create")
    public ResponseEntity<Long> createComment(@Valid CommentCreationForm commentCreationForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addComment(commentCreationForm, ((User) auth.getPrincipal()) ).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PreAuthorize(
            "@authz.isOwner(#post) or @authz.hasAuthorityGe(#post, 'MODERATOR')")
    @PostMapping("/api/post/delete")
    public ResponseEntity<String> deletePost(@RequestParam("id") Post post) {
        postService.deletePost(post);
        return new ResponseEntity<>("deleted thread: " + post.getId(), HttpStatus.OK);
    }

    @PreAuthorize(
            "@authz.isOwner(#comment) or @authz.hasAuthorityGe(#comment, 'MODERATOR')")
    @PostMapping("/api/comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("id") PostComment comment) {
        postService.deleteComment(comment);
        return new ResponseEntity<>("deleted comment: " + comment.getId(), HttpStatus.OK);
    }

    @PostMapping("/api/post/like")
    public ResponseEntity<Boolean> likePost(@RequestParam("id") Post post, Authentication auth) throws EntityNotFoundException {
       return new ResponseEntity<>(
               postService.likePost(post, ( (User) auth.getPrincipal() )),
               HttpStatus.OK);
    }

    @PostMapping("/api/comment/like")
    public ResponseEntity<String> likeComment(@RequestParam("id") PostComment comment, Authentication auth) throws EntityNotFoundException {
        return new ResponseEntity<>(
                "liked:" + postService.likeComment(comment, ( (User) auth.getPrincipal() )),
                HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/post/get_posts")
    public Slice<PostBriefView> getPostSlice(
            @RequestParam(value = "categoryId", required = false)Integer categoryId,
            @PageableDefault(
                    sort = {Post_.LAST_UPDATE_TIME},
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication auth
    ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        return postService.getPostSlice(categoryId, user, pageable, true);
    }

    @GetMapping("/api/no_auth/post/get_comments")
    public Slice<CommentBriefView> getCommentSlice(
            @RequestParam("id") Post post,
            Pageable pageable,
            Authentication auth
            ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        return postService.getBriefCommentSliceByPost(post, user, pageable);
    }

    @GetMapping("/api/no_auth/comment/get_detail")
    public ResponseEntity<CommentDetailedView> getCommentDetail(
            @RequestParam("id") int id,
            Authentication auth
    ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        CommentDetailedView view = postService.getCommentDetailedView(id, user);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/post/thread")
    public ResponseEntity<PostDetailedView> getThread(
            @RequestParam("id") long id, Authentication auth) throws JsonProcessingException {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        PostDetailedView view = postService.getPostDetailedView(id, user);
        if (user != null) view.setViewCount(postService.increaseViewCount(id, 1));
        return new ResponseEntity<>(view, HttpStatus.OK);
    }
}
