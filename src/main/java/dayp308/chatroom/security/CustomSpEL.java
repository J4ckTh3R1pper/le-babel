package dayp308.chatroom.security;

import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
public class CustomSpEL {
    private final CategoryMemberService categoryMemberService;
    private final PostService postService;

    @Autowired
    public CustomSpEL(CategoryMemberService categoryMemberService, PostService postService) {
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    public boolean hasMemberShip(PostCategory category) {
        return categoryMemberService.hasMembership(category,
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
    }

    public boolean hasMembershipGe(PostCategory category, String role) {
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                category,
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                Role.valueOf(role)
        );
    }

    public boolean hasAuthorityGe(Post post, String role) {
        return this.categoryMemberService.hasAuthorityGreaterOrEquals(
                post.getCategory(),
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                Role.valueOf(role));
    }
    
    public boolean hasAuthorityGe(PostComment comment, String role) {
        return hasAuthorityGe(comment.getPost(), role);
    }

    public boolean isPostOwner(Post post) {
        long authenticatedUserId = (
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        ).getId();
        return post.getUser().getId() == authenticatedUserId;
    }

    public boolean isCommentOwner(PostComment comment) {
        long authenticatedUserId = (
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        ).getId();
        return comment.getUser().getId() == authenticatedUserId;
    }


    public boolean hasMembershipEquals(PostCategory category, String role) {
        return this.categoryMemberService.hasAuthority(
                category,
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                Role.valueOf(role)
        );
    }
}
