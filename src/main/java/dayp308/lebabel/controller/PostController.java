package dayp308.lebabel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.lebabel.entity.comment.PostComment;
import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.post.Post_;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.business.CommentCreationForm;
import dayp308.lebabel.entity.business.LikeResponse;
import dayp308.lebabel.entity.business.PostForm;
import dayp308.lebabel.entity.category.PostCategory;
import dayp308.lebabel.entity.view.comment.CommentView;
import dayp308.lebabel.entity.view.post.PostBriefView;
import dayp308.lebabel.entity.view.post.PostDetailedView;
import dayp308.lebabel.repository.*;
import dayp308.lebabel.repository.projection.CommentData;
import dayp308.lebabel.service.FileService;
import dayp308.lebabel.service.PostService;
import dayp308.lebabel.service.SearchService;
import dayp308.lebabel.service.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dayp308.lebabel.entity.view.post.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/no_auth/post")
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

    @GetMapping("/get_posts")
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

    @GetMapping("/get_comments")
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

    @GetMapping("/thread")
    public PostDetailedView getThread(
            @RequestParam("id") long id, Authentication auth) throws JsonProcessingException {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        PostDetailedView view = postService.getPostDetailedView(id, user);
        // if (user != null) view.setViewCount(postService.increaseViewCount(id, 2));
        return view;
    }

    @GetMapping("/get_single")
    public PostBriefView getSingle(@RequestParam("id") long id, Authentication auth) {
        User user = null;
        if ( auth != null && auth.isAuthenticated() )
            user = (User) auth.getPrincipal();

        return postService.getPostBriefView(id, user) ;
    }
    

    @GetMapping("/get_minimal_list")
    public List<PostMinimalView> getMinimalList(@RequestParam("ids") List<Long> ids) {
        return postService.getPostMinimalViewList(ids);
    }

    @GetMapping("/search")
    public List<Long> searchPost(@RequestParam("keyword") String keyword) {
        List<Long> ids = searchService.searchPost(keyword).stream().map(p -> p.getId()).toList();
        return ids;
    }

}
