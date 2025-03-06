package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.view.LikeablePagedResponse;
import dayp308.chatroom.entity.view.PostBriefView;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public PostController(PostService postService, UserService userService, UserRepository userRepository, ObjectMapper jacksonObjectMapper) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @PostMapping("/api/post/create")
    public ResponseEntity<String> createPost(@Valid PostForm postForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addPost(postForm, ( (User) auth.getPrincipal() )).getId();
        return new ResponseEntity<>("thread: " + id, HttpStatus.CREATED);
    }

    @PostMapping("/api/comment/create")
    public ResponseEntity<String> createComment(@Valid CommentCreationForm commentCreationForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addComment(commentCreationForm, ((User) auth.getPrincipal()) ).getId();
        return new ResponseEntity<>("comment: " + id, HttpStatus.CREATED);
    }

    @PreAuthorize("isOwnerOfPost(#postId) or hasAuthorityPostGe(#postId, 'MODERATOR')")
    @PostMapping("/api/post/delete")
    public ResponseEntity<String> deletePost(@RequestParam("postId") long postId, Authentication auth) {
        postService.deletePost(postService.getPostById(postId));
        return new ResponseEntity<>("deleted thread: " + postId, HttpStatus.OK);
    }

    @PreAuthorize("isOwnerOfComment(#commentId) or hasAuthorityCommentGe(#commentId, 'MODERATOR')")
    @PostMapping("/api/comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("commentId") long commentId, Authentication auth) {
        postService.deleteComment(postService.getCommentById(commentId));
        return new ResponseEntity<>("deleted comment: " + commentId, HttpStatus.OK);
    }

    @PostMapping("/api/no_auth/post/get_posts")
    public ResponseEntity<String> getNoAuthPost(@RequestParam("categoryId") int categoryId,
                                                @RequestParam("pageNum") int pageNum,
                                                Authentication auth
                                                ) throws JsonProcessingException {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();
        LikeablePagedResponse<PostBriefView> resp = postService.getPostSliceByCategory(categoryId, pageNum, user);
        return new ResponseEntity<>(jacksonObjectMapper.writeValueAsString(resp), HttpStatus.OK);
    }
}
