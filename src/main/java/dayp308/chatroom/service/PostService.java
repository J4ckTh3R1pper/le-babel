package dayp308.chatroom.service;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.dto.CommentDTO;
import dayp308.chatroom.entity.dto.PostDTO;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ConversionService conversionService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostService(PostRepository postRepository, CategoryService categoryService, UserService userService, ConversionService conversionService, CommentRepository commentRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.conversionService = conversionService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public PostDTO addPost(PostForm form) {
        Post post = conversionService.convert(form, Post.class);
        postRepository.saveAndFlush(post);
        return conversionService.convert(post, PostDTO.class);
    }

    public void deletePost(PostDTO dto) {
        Post post = Objects.requireNonNull(conversionService.convert(dto, Post.class), "Post cannot be null");
        post.setPostStatus((byte) 0);
        postRepository.save(post);
    }

    public CommentDTO addComment(@NotNull CommentCreationForm form) {
        PostComment comment = conversionService.convert(form, PostComment.class);
        return conversionService.convert(commentRepository.saveAndFlush(comment), CommentDTO.class);
    }
}
