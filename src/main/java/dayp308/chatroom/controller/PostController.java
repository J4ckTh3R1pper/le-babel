package dayp308.chatroom.controller;

import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.dto.PostDTO;
import dayp308.chatroom.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/post/create")
    public ResponseEntity<String> createPost(@Valid PostForm postForm) throws EntityNotFoundException {
        long id = postService.addPost(postForm).getId();
        return new ResponseEntity<>("thread: " + id, HttpStatus.CREATED);
    }

    @PostMapping("/api/comment/create")
    public ResponseEntity<String> createComment(@Valid CommentCreationForm commentCreationForm) throws EntityNotFoundException {
        long id = postService.addComment(commentCreationForm).getId();
        return new ResponseEntity<>("comment: " + id, HttpStatus.CREATED);
    }
}
