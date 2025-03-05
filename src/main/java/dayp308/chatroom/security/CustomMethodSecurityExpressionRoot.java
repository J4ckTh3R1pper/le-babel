package dayp308.chatroom.security;

import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.CommentDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
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

    public boolean hasMemberShip(int categoryId) {
        return this.categoryMemberService.hasMembership(categoryId, ((User) this.getPrincipal()).getId());
    }

    public boolean hasMembershipGe(int categoryId, String role) {
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId, ((User) this.getPrincipal()).getId(), Role.valueOf(role)
        );
    }

    public boolean hasAuthorityPostGe(long postId, String role) {
        int categoryId = postService.getPostById(postId).getPostCategoryId();
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId, ((User) this.getPrincipal()).getId(), Role.valueOf(role));
    }

    public boolean isOwnerOfPost(long postId) {
        long authenticatedUserId = ( (User) this.getPrincipal() ).getId();
        return postService.getPostById(postId).getPublishUserId() == authenticatedUserId;
    }

    public boolean isOwnerOfComment(long commentId) {
        long authenticatedUserId = ( (User) this.getPrincipal() ).getId();
        return postService.getCommentById(commentId).getUserId() == authenticatedUserId;
    }

    public boolean hasAuthorityCommentGe(long commentId, String role) {
        CommentDTO comment = postService.getCommentById(commentId);
        return hasAuthorityPostGe(comment.getPostId(), role);
    }

    public boolean hasMembershipEquals(int categoryId, String role) {
        return this.categoryMemberService.hasAuthority(
                categoryId, ((User) this.getPrincipal()).getId(), Role.valueOf(role)
        );
    }
}
