package dayp308.chatroom.repository.specification;

import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.post.Post_;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class PostSpecs {
    private static Predicate isVisible(Root<Post> root, CriteriaBuilder builder) {
        return builder.equal(root.get(Post_.STATUS), (byte) 1);
    }

    private static Predicate likeKeyword(Root<Post> root,CriteriaBuilder builder, String keyword) {
        return builder.like(builder.lower(root.get(Post_.TITLE)), "%" + keyword.toLowerCase() + "%");
    }

    private static Predicate categoryIdEquals(Root<Post> root, CriteriaBuilder builder, int categoryId) {
        return builder.equal(root.join(Post_.CATEGORY).get(PostCategory_.ID), categoryId);
    }

    public static Specification<Post> build(@Nullable Integer categoryId, @Nullable String keyword, boolean visibleOnly) {
        return ((root, query, builder) -> {
            Predicate p = builder.conjunction();
            if ( keyword != null && !keyword.isBlank() )
                p = likeKeyword(root, builder, keyword);
            if ( categoryId != null )
                p = builder.and(p, categoryIdEquals(root, builder, categoryId));
            if ( visibleOnly )
                return builder.and(p, isVisible(root, builder));
            return p;
        });
    }
}
