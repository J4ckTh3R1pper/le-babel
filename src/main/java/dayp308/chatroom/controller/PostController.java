package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.enums.PostOrderType;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.post.Post_;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.LikeResponse;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.view.comment.CommentView;
import dayp308.chatroom.entity.view.post.PostBriefView;
import dayp308.chatroom.entity.view.post.PostDetailedView;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.repository.projection.CommentData;
import dayp308.chatroom.service.FileService;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.SearchService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dayp308.chatroom.entity.view.post.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PostController {

    private final CommentRepository commentRepository;

    private final FileService fileService;

    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SearchService searchService;

    @Autowired
    public PostController(PostService postService, UserService userService, UserRepository userRepository, ObjectMapper jacksonObjectMapper, PostRepository postRepository, PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository, FileService fileService, CommentRepository commentRepository, SearchService searchService) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.fileService = fileService;
        this.commentRepository = commentRepository;
        this.searchService = searchService;
    }

    @PostMapping("/api/post/create")
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostForm postForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addPost(postForm, ( (User) auth.getPrincipal() )).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping("/api/comment/create")
    public ResponseEntity<Long> createComment(@Valid @RequestBody CommentCreationForm commentCreationForm, Authentication auth) throws EntityNotFoundException {
        long id = postService.addComment(commentCreationForm, ((User) auth.getPrincipal()) ).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping("/api/upload_image")
    public String uploadImage(@RequestPart("image") MultipartFile image) {
        String storeName = fileService.uploadImage(image);
        return "/api/images/" + storeName;
    }

    @PreAuthorize(
            "@authz.isPostOwner(#post) or @authz.hasAuthorityGe(#post, 'MODERATOR')")
    @PutMapping("/api/post/delete")
    public ResponseEntity<String> deletePost(@RequestParam("id") @P("post") Post post) {
        postService.deletePost(post);
        return new ResponseEntity<>("deleted thread: " + post.getId(), HttpStatus.OK);
    }

    @PreAuthorize(
            "@authz.isCommentOwner(#comment) or @authz.hasAuthorityGe(#comment, 'MODERATOR')")
    @PostMapping("/api/comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("id") PostComment comment) {
        postService.deleteComment(comment);
        return new ResponseEntity<>("deleted comment: " + comment.getId(), HttpStatus.OK);
    }

    @PutMapping("/api/post/like")
    public LikeResponse likePost(@RequestParam("id") Post post, Authentication auth) throws EntityNotFoundException {
        Boolean liked = postService.likePost(post, ( (User) auth.getPrincipal() ));
        return new LikeResponse(
            postLikeRepository.countByPostId(post.getId()),
            liked
        );
    }

    @PutMapping("/api/comment/like")
    public LikeResponse likeComment(@RequestParam("id") PostComment comment, Authentication auth) throws EntityNotFoundException {
        Boolean liked = postService.likeComment(comment, ( (User) auth.getPrincipal() ));
        return new LikeResponse(
            commentLikeRepository.countByCommentId(comment.getId()),
            liked
        );
    }

    @GetMapping("/api/no_auth/post/get_posts")
    public Slice<PostBriefView> getPostSlice(
            @RequestParam(value = "categoryId", required = false) PostCategory category,
            @RequestParam(value = "userId", required = false) User targetUser,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(
                    sort = {Post_.LAST_UPDATE_TIME},
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication auth
    ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        return postService.getPostSlice(category, user, targetUser, keyword, pageable, true);
    }

    @GetMapping("/api/no_auth/post/get_comments")
    public Slice<CommentView> getCommentSlice(
            @RequestParam("id") Post post,
            Pageable pageable,
            Authentication auth
            ) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        return postService.getBriefCommentSliceByPost(post, user, pageable);
    }

    @GetMapping("/api/no_auth/comment/get_category_id")
    public Integer getCategoryId(@RequestParam("id") PostComment comment) {
        return comment.getPost().getCategory().getId();
    }
    

    @GetMapping("/api/no_auth/comment/get_detail")
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

    @GetMapping("/api/no_auth/comment/get_single")
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

    @GetMapping("/api/no_auth/comment/get_data")
    public CommentData getCommentData(@RequestParam("id") long id, Authentication auth) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();
        return commentRepository.getCommentDataById(id, user);
    }
    

    @GetMapping("/api/no_auth/post/thread")
    public PostDetailedView getThread(
            @RequestParam("id") long id, Authentication auth) throws JsonProcessingException {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        PostDetailedView view = postService.getPostDetailedView(id, user);
        // if (user != null) view.setViewCount(postService.increaseViewCount(id, 2));
        return view;
    }

    @GetMapping("/api/no_auth/post/get_single")
    public PostBriefView getSingle(@RequestParam("id") long id, Authentication auth) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();


        return postService.getPostBriefView(id, user) ;
    }
    

    @GetMapping("/api/no_auth/post/get_minimal_list")
    public List<PostMinimalView> getMinimalList(@RequestParam("ids") List<Long> ids) {
        return postService.getPostMinimalViewList(ids);
    }

    @GetMapping("/api/no_auth/post/search")
    public List<Long> searchPost(@RequestParam("keyword") String keyword) {
        List<Long> ids = searchService.searchPost(keyword).stream().map(p -> p.getId()).toList();
        return ids;
    }

}
