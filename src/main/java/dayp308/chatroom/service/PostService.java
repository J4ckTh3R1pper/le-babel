package dayp308.chatroom.service;

import dayp308.chatroom.entity.*;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.comment.CommentDTO;
import dayp308.chatroom.entity.comment.CommentIdOnly;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.post.PostDTO;
import dayp308.chatroom.entity.enums.PostOrderType;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.id.CommentLikeId;
import dayp308.chatroom.entity.id.PostLikeId;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.post.PostIdOnly;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.view.comment.CommentUtil;
import dayp308.chatroom.entity.view.comment.CommentView;
import dayp308.chatroom.entity.view.post.PostBriefView;
import dayp308.chatroom.entity.view.post.PostDetailedView;
import dayp308.chatroom.entity.view.post.PostMinimalView;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryMutedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.exception.UserMutedException;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.repository.listener.CommentCreateEvent;
import dayp308.chatroom.repository.projection.CommentProjection;
import dayp308.chatroom.repository.projection.PostProjection;
import dayp308.chatroom.repository.specification.CommentSpecs;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    private final CommentClosureRepository commentClosureRepository;

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
    private final CommentUtil commentUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public PostService(PostRepository postRepository, CategoryService categoryService, UserService userService, ConversionService conversionService, CommentRepository commentRepository, UserRepository userRepository, CategoryRepository categoryRepository, CategoryMemberRepository categoryMemberRepository, PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository, CommentUtil commentUtil, CommentClosureRepository commentClosureRepository, ApplicationEventPublisher eventPublisher) {
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
        this.commentUtil = commentUtil;
        this.commentClosureRepository = commentClosureRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(long id) {
        return postRepository.findById(id, PostDTO.class).orElseThrow();
    }
    @Transactional(readOnly = true)
    public PostIdOnly getPostIdsById(long id){
        return postRepository.findById(id, PostIdOnly.class).orElseThrow();
    }

    @Transactional(readOnly = true)
    public CommentDTO getCommentById(long id) {
        return commentRepository.findById(id, CommentDTO.class).orElseThrow();
    }

    @Transactional(readOnly = true)
    public CommentIdOnly getCommentIdsById(long id) {
        return commentRepository.findById(id, CommentIdOnly.class).orElseThrow();
    }

    @Transactional
    public boolean likePost(Post post, User user) {
        PostLikeId id = new PostLikeId(user.getId(), post.getId());
        if (!postLikeRepository.existsById(id))
            postLikeRepository.saveAndFlush(new PostLike(post, user));
        else {
            postLikeRepository.deleteById(id);
            return false;
        }
        return true;
    }

    @Transactional
    public boolean likeComment(PostComment comment, User user) {
        CommentLikeId id = new CommentLikeId(user.getId(), comment.getId());
        if (!commentLikeRepository.existsById(id))
            commentLikeRepository.saveAndFlush(new CommentLike(comment, user));
        else {
            commentLikeRepository.deleteById(id);
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public PostDetailedView getPostDetailedView(long id, @Nullable User user) {
        PostProjection post = postRepository.findProjById(id, user);
        PostDetailedView view = conversionService.convert(
                post,
                PostDetailedView.class
        );
        return view;
    }

    @Transactional(readOnly = true)
    public PostMinimalView getPostMinimalView(long id) {
        PostProjection post = postRepository.findProjById(id, null);
        PostMinimalView view = conversionService.convert(post, PostMinimalView.class);
        return view;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<PostMinimalView> getPostMinimalViewList(List<Long> ids) {
        List<PostProjection> posts = postRepository.findAllProjByIds(ids);
        Object views = conversionService.convert(posts,
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostProjection.class)),
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostMinimalView.class))
        );
        return (List<PostMinimalView>) views;
    }
    
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Slice<PostBriefView> getPostSlice(
            @Nullable Integer categoryId,
            @Nullable User user,
            Pageable pageable,
            boolean visibleOnly
    ) {
        Slice<PostProjection> slice = postRepository.findAllProj(categoryId, user, visibleOnly, pageable);
        Object views = conversionService.convert(slice.getContent(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostProjection.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostBriefView.class))
        );
        return new SliceImpl<>((List<PostBriefView>) views, pageable, slice.hasNext());
    }

    @Transactional
    public Long increaseViewCount(long id, long count) {
        Post post = postRepository.getReferenceById(id);
        post.setViewCount(post.getViewCount() + count);
        postRepository.saveAndFlush(post);
        return post.getViewCount();
    }

    /*** 获取评论的详细信息，包括所有回复
     *
     * @param commentId 评论ID
     * @return 返回给前端的View对象
     */
    @Transactional(readOnly = true)
    public CommentView getCommentDetailedView(long commentId, @Nullable User user) {
        CommentProjection projection = commentRepository.findProjectionById(commentId, user == null ? null : user.getId());
        CommentView view = commentUtil.getCommentDetailedView(projection, user);
        return view;
    }

    /*** 获取评论列表并分页，如果用户已登录则同时返回用户点赞与否
     *
     * @param post 要获取评论的帖子对象
     * @param pageable 分页对象
     * @param user Controller层返回的已登录用户对象，未登录为null
     * @return 分页视图对象
     */
    @Transactional(readOnly = true)
    public Slice<CommentView> getBriefCommentSliceByPost(
            Post post, @Nullable User user, Pageable pageable) {
        Slice<CommentProjection> projections = commentRepository.findAllProjByPostId(post.getId(), user, pageable, true);
        List<CommentView> views = projections.getContent().stream().map(
                c -> commentUtil.getCommentBriefView(c, user)
        ).toList();
        return new SliceImpl<>(views, pageable, projections.hasNext());
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<CommentDTO> getCommentsByPostId(long id) {
        Post post = postRepository.getReferenceById(id);
        PageRequest pageRequest = PageRequest.of(0, 25);
        Slice<PostComment> slice = commentRepository.findAll(
                CommentSpecs.allCommentByPost(post, false),
                pageRequest);

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
        post.setUser(user);
        postRepository.saveAndFlush(post);
        if ( membership.getRole().ordinal() > 0 ) {
            membership.setExperience(membership.getExperience() + 3);
            categoryMemberRepository.saveAndFlush( membership );
        }
        return postRepository.findById(post.getId(), PostDTO.class).orElseThrow();
    }

    @Transactional
    public CommentDTO addComment(@NotNull CommentCreationForm form, User user) {
        PostComment comment = conversionService.convert(form, PostComment.class);
        Post post = comment.getPost();
        PostCategory category = post.getCategory();
        checkAvailability(category);
        CategoryMember membership = categoryMemberRepository.findById(new CategoryMemberId(category.getId(), user.getId())).orElse(null);
        membership = checkAndCreateMembership(user, post, membership);
        checkMute(category, membership);
        comment.setUser(user);
        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreateEvent(comment));
        if ( membership.getRole().ordinal() > 0 ) {
            membership.setExperience(membership.getExperience() + 3);
            categoryMemberRepository.saveAndFlush( membership );
        }
        post.setLastUpdateTime(Instant.now());
        postRepository.saveAndFlush(post);
        commentRepository.flush();
        commentClosureRepository.flush();
        return commentRepository.findById(comment.getId(), CommentDTO.class).orElseThrow();
    }

    public void deletePost(Post post) {
        post.setStatus((byte) 0);
        postRepository.saveAndFlush(post);
    }

    public void deleteComment(PostComment dto) {
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
