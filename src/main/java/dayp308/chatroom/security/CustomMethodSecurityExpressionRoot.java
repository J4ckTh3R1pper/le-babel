package dayp308.chatroom.security;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.CommentDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

// https://stackoverflow.com/questions/77236793/custom-method-in-preauthorize
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot {

    private final CategoryMemberService categoryMemberService;
    private final PostService postService;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, CategoryMemberService categoryMemberService, PostService postService) {
        this(() ->  authentication, categoryMemberService, postService);
    }

    public CustomMethodSecurityExpressionRoot(Supplier<Authentication> authentication, CategoryMemberService categoryMemberService, PostService postService) {
        super(authentication);
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    public CustomMethodSecurityExpressionRoot(MethodSecurityExpressionOperations delegate, CategoryMemberService categoryMemberService, PostService postService) {
        super(delegate.getAuthentication());
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    public boolean hasMemberShip(int categoryId) {
        return this.categoryMemberService.hasMembership(categoryId, ((User) this.getPrincipal()));
    }

    public boolean hasMembershipGe(int categoryId, String role) {
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId, ((User) this.getPrincipal()), Role.valueOf(role)
        );
    }

    public boolean hasAuthorityPostGe(long postId, String role) {
        int categoryId = postService.getPostIdsById(postId).getCategory().getId();
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId, ((User) this.getPrincipal()), Role.valueOf(role));
    }

    public boolean isOwnerOfPost(long postId) {
        long authenticatedUserId = ( (User) this.getPrincipal() ).getId();
        return postService.getPostIdsById(postId).getPublishUser().getId() == authenticatedUserId;
    }

    public boolean isOwnerOfComment(long commentId) {
        long authenticatedUserId = ( (User) this.getPrincipal() ).getId();
        return postService.getCommentIdsById(commentId).getUser().getId() == authenticatedUserId;
    }

    public boolean hasAuthorityCommentGe(long commentId, String role) {
        CommentDTO comment = postService.getCommentById(commentId);
        return hasAuthorityPostGe(comment.getPost().getId(), role);
    }

    public boolean hasMembershipEquals(int categoryId, String role) {
        return this.categoryMemberService.hasAuthority(
                categoryId, ((User) this.getPrincipal()), Role.valueOf(role)
        );
    }
}
