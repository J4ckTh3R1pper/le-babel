package dayp308.lebabel.repository.specification;

import dayp308.lebabel.entity.comment.PostComment;
import dayp308.lebabel.entity.comment.PostComment_;
import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.post.Post_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class CommentSpecs {

    static Predicate isDeleted(CriteriaBuilder builder, Root<PostComment> root) {
        return builder.isTrue(root.get(PostComment_.IS_DELETED));
    }

    public static Specification<PostComment> allCommentByPost(Post post, boolean isDeleted) {
        return ((root, query, builder) -> {
            long postId = post.getId();
            Predicate equalId = builder.equal(root.join(PostComment_.POST).get(Post_.ID), postId);
            if ( isDeleted ) return builder.and(equalId, isDeleted(builder, root));
            return equalId;
        });
    }

    public static Specification<PostComment> nonSubCommentByPost(Post post, boolean isDeleted) {
        return ((root, query, builder) -> {
            long postId = post.getId();
            Predicate equalId = builder.equal(root.join(PostComment_.POST).get(Post_.ID), postId);
            Predicate nonSubComment = builder.isNull(root.get(PostComment_.PARENT_COMMENT));
            if ( isDeleted ) return builder.and(equalId, nonSubComment, isDeleted(builder, root));
            return builder.and(equalId, nonSubComment);
        });
    }

    public static Specification<PostComment> allSubCommentByComment(PostComment parent, boolean isDeleted) {
        return ((root, query, builder) -> {
            long parentId = parent.getId();
            Predicate equalId =  builder.equal(root.join(PostComment_.PARENT_COMMENT).get(PostComment_.ID), parentId);
            if ( isDeleted ) return builder.and(equalId, isDeleted(builder, root));
            return equalId;
        });
    }

}
