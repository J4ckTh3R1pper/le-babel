package dayp308.chatroom.service;

import dayp308.chatroom.entity.*;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.CommentDTO;
import dayp308.chatroom.entity.PostDTO;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.id.CommentLikeId;
import dayp308.chatroom.entity.id.PostLikeId;
import dayp308.chatroom.entity.view.LikeablePagedResponse;
import dayp308.chatroom.entity.view.PostBriefView;
import dayp308.chatroom.entity.view.comment.CommentBriefView;
import dayp308.chatroom.entity.view.comment.CommentDetailResponse;
import dayp308.chatroom.entity.view.comment.CommentDetailedView;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryMutedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.exception.UserMutedException;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.repository.specification.CustomSpecifications;
import dayp308.chatroom.util.PageUtil;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ConversionService conversionService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMemberRepository categoryMemberRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    public PostService(PostRepository postRepository, CategoryService categoryService, UserService userService, ConversionService conversionService, CommentRepository commentRepository, UserRepository userRepository, CategoryRepository categoryRepository, CategoryMemberRepository categoryMemberRepository, PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository) {
        this.postRepository = postRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.conversionService = conversionService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMemberRepository = categoryMemberRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(long id) {
        return postRepository.findById(id, PostDTO.class);
    }

    @Transactional(readOnly = true)
    public CommentDTO getCommentById(long id) {
        return commentRepository.findById(id, CommentDTO.class);
    }

    /*** 获取评论的详细信息，包括所有回复
     *
     * @param commentId 评论ID
     * @return 返回给前端的View对象
     */
    @Transactional(readOnly = true)
    public CommentDetailResponse getCommentDetailedView(long commentId, @Nullable User user) {
        PostComment comment = (PostComment) Hibernate.unproxy(commentRepository.getReferenceById(commentId));
        CommentDetailedView view = conversionService.convert(comment, CommentDetailedView.class);
        CommentDetailResponse response = new CommentDetailResponse();
        response.setView(view);
        if (user != null) {
            Set<CommentLikeId> commentIds = new HashSet<>();
            view.dfs( c -> {
                commentIds.add( new CommentLikeId(user.getId(), c.getId()) );
            });
            commentIds.add(new CommentLikeId(user.getId(), commentId));
            response.setLikedIds(
                    commentLikeRepository.findAllById(commentIds)
                            .stream()
                            .map(l -> l.getComment().getId()).collect(Collectors.toSet()));
        }
        return response;
    }

    @Transactional(readOnly = true)
    public CommentBriefView getCommentBriefView(long commentId) {
        PostComment comment = (PostComment) Hibernate.unproxy(commentRepository.getReferenceById(commentId));
        System.out.println(conversionService.canConvert(PostComment.class, CommentBriefView.class));
        return conversionService.convert(comment, CommentBriefView.class);
    }

    /*** 获取评论列表并分页，如果用户已登录则同时返回用户点赞与否
     *
     * @param postId 要获取评论的帖子ID
     * @param pageNum 页码
     * @param pageSize 每页元素数量
     * @param user Controller层返回的已登录用户对象，未登录为null
     * @return 分页视图对象
     */
    @Transactional(readOnly = true)
    public LikeablePagedResponse<CommentBriefView> getBriefCommentSliceByPost(long postId, int pageNum, int pageSize, User user) {
        Post post = postRepository.getReferenceById(postId);
        Slice<PostComment> slice = commentRepository.findAll(
                CustomSpecifications.nonSubCommentByPostId(post),
                PageRequest.of(pageNum, pageSize));

        List<CommentBriefView> content = (List<CommentBriefView>) conversionService.convert(slice.getContent(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostComment.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentBriefView.class))
                );

        LikeablePagedResponse<CommentBriefView> resp = PageUtil.sliceToLikeResponse(slice, content);
        if (user == null) resp.setLikedIds(new HashSet<>());
        else resp.setLikedIds(getCommentBriefLikeMap(content, user));
        return resp;
    }

    @Transactional(readOnly = true)
    public Set<Long> getCommentBriefLikeMap(List<CommentBriefView> comments, User user) {
        Set<CommentLikeId> likeIds = new HashSet<>();
        comments.forEach(comment -> {
            comment.getChildren().forEach(child ->
                    likeIds.add(new CommentLikeId(comment.getId(), child.getId())));
            likeIds.add(new CommentLikeId(user.getId(),comment.getId()));
        });
        return commentLikeRepository.findAllById(likeIds)
                .stream().map( l -> l.getComment().getId())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public LikeablePagedResponse<PostBriefView> getPostSliceByCategory(int categoryId, int pageNum, @Nullable User user) {
        Slice<Post> slice = postRepository.findAllByCategoryIdOrderByLastUpdateTimeDesc(categoryId, PageRequest.of(pageNum, 15));
        List<PostBriefView> views = (List<PostBriefView>) conversionService.convert(slice.getContent(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostBriefView.class))
                );
        LikeablePagedResponse<PostBriefView> resp = PageUtil.sliceToLikeResponse(slice, views);
        if (user != null) resp.setLikedIds(getPostBriefLikeSet(slice.getContent(), user));
            else resp.setLikedIds(new HashSet<>());
        return resp;
    }

    @Transactional(readOnly = true)
    public Set<Long> getPostBriefLikeSet(List<Post> posts, User user) {
        Set<PostLikeId> likeIds = new HashSet<>();
        posts.forEach(post -> {
            likeIds.add(new PostLikeId(post.getId(), user.getId()));
        });
        return postLikeRepository.findAllById(likeIds)
                .stream().map( l -> l.getPost().getId())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPostId(long id) {
        Post post = postRepository.getReferenceById(id);
        PageRequest pageRequest = PageRequest.of(0, 25);
        Slice<PostComment> slice = commentRepository.findAll(CustomSpecifications.allCommentByPostNotDeleted(post), pageRequest);

        // https://stackoverflow.com/questions/7738305/spring-conversion-service-from-lista-to-listb
        return (List<CommentDTO>) conversionService.convert(
                        slice.getContent(),
                        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostComment.class)),
                        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(CommentDTO.class))
        );
    }

    @Transactional
    public PostDTO addPost(@NotNull PostForm form, User user) {
        Post post = Objects.requireNonNull(conversionService.convert(form, Post.class));
        PostCategory category = categoryRepository.getReferenceById(form.getCategoryId());
        checkAvailability(category);
        CategoryMember membership = categoryMemberRepository.findById(new CategoryMemberId(category.getId(), user.getId())).orElse(null);
        membership = checkAndCreateMembership(user, post, membership);
        checkMute(category, membership);
        post.setPublishUser(user);
        postRepository.saveAndFlush(post);
        membership.setExperience(membership.getExperience() + 3);
        categoryMemberRepository.saveAndFlush( membership );
        return postRepository.findById(post.getId(), PostDTO.class);
    }

    @Transactional
    public CommentDTO addComment(@NotNull CommentCreationForm form, User user) {
        PostComment comment = conversionService.convert(form, PostComment.class);
        Post post = postRepository.getReferenceById(form.getPostId());
        PostCategory category = post.getCategory();
        checkAvailability(category);
        CategoryMember membership = categoryMemberRepository.findById(new CategoryMemberId(category.getId(), user.getId())).orElse(null);
        membership = checkAndCreateMembership(user, post, membership);
        checkMute(category, membership);
        comment.setUser(user);
        commentRepository.saveAndFlush(comment);
        membership.setExperience(membership.getExperience() + 3);
        categoryMemberRepository.saveAndFlush( membership );
        post.setLastUpdateTime(Instant.now());
        postRepository.saveAndFlush(post);
        return commentRepository.findById(comment.getId(), CommentDTO.class);
    }

    public void addPostLike(long postId, User user) {
        PostLike like = new PostLike();
        like.setPost(postRepository.getReferenceById(postId));
        like.setUser(user);
        try {
            postLikeRepository.saveAndFlush(like);
        } catch (EntityExistsException e) {
            throw new EntityExistsException("Post already liked");
        }
    }

    public void addCommentLike(long commentId, User user) {
        CommentLike like = new CommentLike();
        like.setComment(commentRepository.getReferenceById(commentId));
        like.setUser(user);
        try {
            commentLikeRepository.saveAndFlush(like);
        } catch (EntityExistsException e) {
            throw new EntityExistsException("Comment already liked");
        }
    }

    public void deletePost(PostDTO dto) {
        Post post = Objects.requireNonNull(conversionService.convert(dto, Post.class), "Post cannot be null");
        post.setStatus((byte) 0);
        postRepository.saveAndFlush(post);
    }

    public void deleteComment(CommentDTO dto) {
        PostComment comment = Objects.requireNonNull(conversionService.convert(dto, PostComment.class));
        comment.setIsDeleted(true);
        commentRepository.saveAndFlush(comment);
    }

    private void checkAvailability(PostCategory postCategory) {
        if (postCategory.getIsDeleted()) throw new CategoryDeletedException("category has been deleted");
        if (postCategory.getIsPending()) throw new CategoryPendingException("category is currently pending");
    }

    private void checkMute(PostCategory postCategory, CategoryMember user) {
        if (postCategory.getIsMuted() ) throw new CategoryMutedException("category is muted");
        if (user.getMuteExpirationDate().isAfter(Instant.now()) ) throw new UserMutedException("user has been muted", user.getMuteExpirationDate());
    }

    private CategoryMember checkAndCreateMembership(User user, Post post, CategoryMember membership) {
        if ( membership == null ) {
            membership = new CategoryMember();
            membership.setUser(user);
            membership.setCategory(post.getCategory());
            categoryMemberRepository.saveAndFlush( membership );
        }
        return membership;
    }
}
