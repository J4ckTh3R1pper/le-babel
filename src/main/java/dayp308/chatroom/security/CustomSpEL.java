package dayp308.chatroom.security;

import dayp308.chatroom.entity.comment.CommentDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("authz")
public class CustomSpEL {
    private final CategoryMemberService categoryMemberService;
    private final PostService postService;

    @Autowired
    public CustomSpEL(CategoryMemberService categoryMemberService, PostService postService) {
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    public boolean hasMemberShip(MethodSecurityExpressionOperations root, int categoryId) {
        return categoryMemberService.hasMembership(categoryId,
                (User) root.getAuthentication().getPrincipal()
        );
    }

    public boolean hasMembershipGe(MethodSecurityExpressionOperations root, int categoryId, String role) {
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId,
                (User) root.getAuthentication().getPrincipal(),
                Role.valueOf(role)
        );
    }

    public boolean hasAuthorityPostGe(MethodSecurityExpressionOperations root, long postId, String role) {
        int categoryId = postService.getPostIdsById(postId).getCategory().getId();
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                categoryId,
                (User) root.getAuthentication().getPrincipal(),
                Role.valueOf(role));
    }

    public boolean isOwnerOfPost(MethodSecurityExpressionOperations root, long postId) {
        long authenticatedUserId = (
            (User) root.getAuthentication().getPrincipal()
        ).getId();
        return postService.getPostIdsById(postId).getPublishUser().getId() == authenticatedUserId;
    }

    public boolean isOwnerOfComment(MethodSecurityExpressionOperations root, long commentId) {
        long authenticatedUserId = (
            (User) root.getAuthentication().getPrincipal()
        ).getId();
        return postService.getCommentIdsById(commentId).getUser().getId() == authenticatedUserId;
    }

    public boolean hasAuthorityCommentGe(MethodSecurityExpressionOperations root, long commentId, String role) {
        CommentDTO comment = postService.getCommentById(commentId);
        return hasAuthorityPostGe(root, comment.getPost().getId(), role);
    }

    public boolean hasMembershipEquals(MethodSecurityExpressionOperations root, int categoryId, String role) {
        return this.categoryMemberService.hasAuthority(
                categoryId,
                (User) root.getAuthentication().getPrincipal(),
                Role.valueOf(role)
        );
    }
}
