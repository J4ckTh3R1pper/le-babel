package dayp308.chatroom.repository.specification;

import dayp308.chatroom.entity.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class CustomSpecifications {

    static Predicate commentNotDeleted(CriteriaBuilder builder, Root<PostComment> root) {
        return builder.notEqual(root.get(PostComment_.IS_DELETED), true);
    }

    public static Specification<CategoryMember> userMemberships(User user) {
        return (root, query, builder) -> {
            long userId = user.getId();
            return builder.equal(root.join(CategoryMember_.USER).get(User_.ID), userId);
        };
    }

    public static Specification<PostComment> allCommentByPostNotDeleted(Post post) {
        return ((root, query, builder) -> {
            long postId = post.getId();
            Predicate equalId =  builder.equal(root.join(PostComment_.POST).get(Post_.ID), postId);
            return builder.and(equalId, commentNotDeleted(builder, root));
        });
    }

    public static Specification<PostComment> nonSubCommentByPostId(Post post) {
        return ((root, query, builder) -> {
            long postId = post.getId();
            Predicate equalId = builder.equal(root.join(PostComment_.POST).get(Post_.ID), postId);
            Predicate nonSubComment = builder.isNull(root.get(PostComment_.PARENT_COMMENT));
            return builder.and(equalId, commentNotDeleted(builder, root), nonSubComment);
        });
    }

    public static Specification<PostComment> allSubCommentByComment(PostComment parent) {
        return ((root, query, builder) -> {
            long parentId = parent.getId();
            Predicate equalId =  builder.equal(root.join(PostComment_.PARENT_COMMENT).get(PostComment_.ID), parentId);
            return builder.and(equalId, commentNotDeleted(builder, root));
        });
    }

}
