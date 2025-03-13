package dayp308.chatroom.repository.specification;

import dayp308.chatroom.entity.member.CategoryMember_;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.user.User_;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.user.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecs {
    public static Specification<PostCategory> getAllCategories(User user, String orderBy, boolean ascending) {
        return (root, query, builder) -> {
            if (query == null) query = builder.createTupleQuery();
            Join<PostCategory, CategoryMember> m = root.join(PostCategory_.CATEGORY_MEMBERS, JoinType.LEFT);
            Predicate where = builder.conjunction();
            if (user != null) {
                switch (orderBy) {
                    case CategoryMember_.EXPERIENCE -> orderByExperience(m, query, builder, ascending);
                    case CategoryMember_.JOIN_DATE -> orderByJoinDate(m, query, builder, ascending);
                    default -> orderByRank(root, query, builder, ascending);
                }
                where = builder.equal(m.join(CategoryMember_.USER).get(User_.ID), user.getId());
            } else orderByRank(root, query, builder, ascending);
            return where;
        };
    }

    public static void orderByExperience(Join<PostCategory, CategoryMember> join, CriteriaQuery query,  CriteriaBuilder cb, boolean ascending) {
        query.orderBy(
                ascending ? cb.asc(join.get(CategoryMember_.EXPERIENCE))
                          : cb.desc(join.get(CategoryMember_.EXPERIENCE))
        );
    }

    public static void orderByJoinDate(Join<PostCategory, CategoryMember> join, CriteriaQuery query,  CriteriaBuilder cb, boolean ascending) {
        query.orderBy(
                ascending ? cb.asc(join.get(CategoryMember_.JOIN_DATE))
                        : cb.desc(join.get(CategoryMember_.JOIN_DATE))
        );
    }

    public static void orderByRank(Root<PostCategory> root, CriteriaQuery query,  CriteriaBuilder cb, boolean ascending) {
        query.orderBy(
                ascending ? cb.asc(root.get(PostCategory_.RANK))
                        : cb.desc(root.get(PostCategory_.RANK))
        );
    }

}
